package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.Authentication.OAuthTokenResponse;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.model.UserStatus;
import com.projectct.authservice.repository.UserRepository;
import com.projectct.authservice.repository.UserStatusRepository;
import com.projectct.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService{
    final UserRepository userRepository;
    final UserStatusRepository userStatusRepository;
    final JwtUtil jwtUtil;
    final UserMapper userMapper;
    final RestTemplate restTemplate = new RestTemplate();
    final OAuth2AuthorizedClientService authorizedClientService;

    @Transactional
    @Override
    public OAuthTokenResponse processGithubLogin(OAuth2User oAuth2User) {
//        log.error(oAuth2User.getAttributes().toString());
        String githubId = oAuth2User.getAttribute("id").toString();
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");

        User ghUser = userRepository.findByGithubId(githubId);
        if (ghUser != null)
            return OAuthTokenResponse.builder()
                    .token(jwtUtil.generateAccessToken(ghUser.getUsername()))
                    .updated(ghUser.getStatus() != null && ghUser.getStatus().isActivated())
                    .build();

        String email = null;
        if (oAuth2User.getAttribute("email") == null) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    authToken.getAuthorizedClientRegistrationId(),
                    authToken.getName()
            );

            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            for (Map<String, Object> mail : response.getBody()) {
                if (Boolean.TRUE.equals(mail.get("primary"))) {
                    email = (String) mail.get("email");
                    break;
                }
            }
        } else {
            email = oAuth2User.getAttribute("email");
        }

        User newGithubUser = User.builder()
                .username(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")))
                .name(name)
                .avatarURL(avatarUrl)
                .email(email)
                .githubId(githubId)
                .build();

        User savedGithubUser = userRepository.save(newGithubUser);

        UserStatus newUserStatus = UserStatus.builder()
                .newUser(true)
                .activated(false)
                .build();

        newUserStatus.setUser(savedGithubUser);
        userStatusRepository.save(newUserStatus);
        savedGithubUser.setStatus(newUserStatus);

        userRepository.save(savedGithubUser);

        return OAuthTokenResponse.builder()
                .token(jwtUtil.generateAccessToken(savedGithubUser.getUsername()))
                .updated(false)
                .build();
    }

    @Override
    public OAuthTokenResponse processGoogleLogin(OAuth2User oAuth2User) {
//        log.error(oAuth2User.getAttributes().toString());
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");

        User ggUser = userRepository.findByEmail(email);
        if (ggUser != null){
            if (ggUser.getIsGoogleAccount()!=null && !ggUser.getIsGoogleAccount()) {
                ggUser.setIsGoogleAccount(true);
                userRepository.save(ggUser);
            }
            return OAuthTokenResponse.builder()
                    .token(jwtUtil.generateAccessToken(ggUser.getUsername()))
                    .updated(ggUser.getStatus() != null && ggUser.getStatus().isActivated())
                    .build();
        }

        User newGoogleUser = User.builder()
                .username(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")))
                .name(name)
                .avatarURL(avatarUrl)
                .email(email)
                .isGoogleAccount(true)
                .build();

        User savedGoogleUser = userRepository.save(newGoogleUser);

        UserStatus newUserStatus = UserStatus.builder()
                .newUser(true)
                .activated(false)
                .build();

        newUserStatus.setUser(savedGoogleUser);
        userStatusRepository.save(newUserStatus);
        savedGoogleUser.setStatus(newUserStatus);

        userRepository.save(savedGoogleUser);

        return OAuthTokenResponse.builder()
                .token(jwtUtil.generateAccessToken(savedGoogleUser.getUsername()))
                .updated(false)
                .build();
    }
}
