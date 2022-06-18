package uz.in_trade_map.service;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

@Component
public class PasswordActions {

    public String encodePassword(String password) {
        return Base64.getEncoder()
                .encodeToString(password.getBytes());
    }

    public String decodePassword(String encodedPassword) {
        return new String(Base64.getDecoder()
                .decode(encodedPassword));
    }

    public String generatePassword() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
