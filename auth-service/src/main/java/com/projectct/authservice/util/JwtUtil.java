package com.projectct.authservice.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    @Value("${jwt.refreshable_time}")
    private long refreshableTime;
    @Value("${jwt.valid_time}")
    private long validTime;

    final UserRepository userRepository;

    public String generateAccessToken(String username) {
        var user = userRepository.findByUsernameOrEmail(username, username);
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validTime, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("id", user.getId())
                .claim("rft", generateRefreshToken(username))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(String username) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(refreshableTime, ChronoUnit.DAYS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public JWTClaimsSet getClaimsSet(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimsSet(token).getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);

            return verified && expiredTime.after(new Date());
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse refreshToken(String oldToken) {
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(oldToken);
            String refreshToken = signedJWT.getJWTClaimsSet().getStringClaim("rft");

            if (isValidToken(refreshToken)) {
                String username = getUsernameFromToken(refreshToken);
                return AuthenticationResponse.builder()
                        .token(generateAccessToken(username))
                        .build();
            } else
                throw new AppException(HttpStatus.UNAUTHORIZED, "Refresh token failed");
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            return getClaimsSet(token).getLongClaim("id");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
