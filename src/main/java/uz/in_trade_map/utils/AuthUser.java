package uz.in_trade_map.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUser {
    public static Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
