package uz.in_trade_map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.in_trade_map.dtos.ResToken;
import uz.in_trade_map.dtos.SignIn;
import uz.in_trade_map.dtos.UserDto;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.payload.ApiResponse;
import uz.in_trade_map.secret.CurrentUser;
import uz.in_trade_map.service.AuthService;

@RestController
@Controller
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthService authService;

    //    @Autowired
//    CategoryRepository categoryRepository;
////
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody SignIn signIn) {
        ResToken resToken = authService.signIn(signIn);
        return ResponseEntity.status(resToken != null ? 200 : 401).body(
                resToken != null ? new ApiResponse(1, "Success", resToken)
                        :
                        new ApiResponse(0, "User unauthorized")
        );
    }

    @GetMapping("/me")
    public HttpEntity<?> getMe(@CurrentUser User user) {
        return ResponseEntity.ok(new ApiResponse(1, "Success", UserDto.response(user)));
    }
}
