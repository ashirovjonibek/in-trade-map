package uz.in_trade_map.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class AuthUser {
    public static Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
