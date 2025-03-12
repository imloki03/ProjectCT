package com.projectct.messageservice.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class FeignRequestContextUtil {
    private static final ThreadLocal<String> authToken = new ThreadLocal<>();

    public static void setAuthToken(String token) {
        authToken.set(token);
    }

    public static String getAuthToken() {
        return authToken.get();
    }

    public static void clear() {
        authToken.remove(); // Clean up to prevent memory leaks
    }
}
