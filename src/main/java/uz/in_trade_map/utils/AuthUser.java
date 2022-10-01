package uz.in_trade_map.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.entity.enums.RoleName;

@Data
@AllArgsConstructor
public class AuthUser {
    private User user;
    private boolean admin;

    public AuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))) {
            User currentUser = (User) authentication.getPrincipal();
            this.user = currentUser;
            this.admin = currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_ADMIN.name()));
        }
    }

    public static Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
