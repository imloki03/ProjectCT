package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.OAuthTokenResponse;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthService {
    OAuthTokenResponse processGithubLogin(OAuth2User oAuth2User);
    OAuthTokenResponse processGoogleLogin(OAuth2User oAuth2User);
}
