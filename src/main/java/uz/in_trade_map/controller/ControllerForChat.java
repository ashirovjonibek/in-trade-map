package uz.in_trade_map.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.secret.CurrentUser;

@RestController
@RequestMapping("/chat-app")
@CrossOrigin("http://localhost:9090")
public class ControllerForChat {
    @GetMapping("/current-user")
    public HttpEntity<?> getCurrentUser(@CurrentUser User user) {
        if (user != null) {
            return AllApiResponse.response(1, "Current user", user.getId());
        } else return AllApiResponse.response(403, 0, "User not permitted!");
    }
}
