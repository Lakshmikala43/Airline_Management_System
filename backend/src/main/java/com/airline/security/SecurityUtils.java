package com.airline.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class SecurityUtils {

    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getUsername());
        }
        return Optional.ofNullable(authentication.getName());
    }

    public static Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getId());
        }
        return Optional.empty();
    }
}
