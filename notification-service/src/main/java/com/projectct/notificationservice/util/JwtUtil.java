package com.projectct.notificationservice.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
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

    public Long getUserIdFromToken(String token) {
        try {
            return getClaimsSet(token).getLongClaim("id");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
