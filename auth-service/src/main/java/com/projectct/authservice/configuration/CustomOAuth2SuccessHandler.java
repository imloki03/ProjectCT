package com.projectct.authservice.configuration;

import com.projectct.authservice.service.OAuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Value("${domain.fe}")
    private String frontendDomain;

    final OAuthService oAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();

        if (Objects.equals(registrationId, "github")) {
            var oauthToken = oAuthService.processGithubLogin((OAuth2User) authentication.getPrincipal());
            response.sendRedirect(frontendDomain+"/oauth2/github/redirect?token="+oauthToken.getToken()+"&updated="+oauthToken.isUpdated());
        }
        if (Objects.equals(registrationId, "google")) {
            var oauthToken = oAuthService.processGoogleLogin((OAuth2User) authentication.getPrincipal());
            response.sendRedirect(frontendDomain+"/oauth2/google/redirect?token="+oauthToken.getToken()+"&updated="+oauthToken.isUpdated());
        }
    }
}
