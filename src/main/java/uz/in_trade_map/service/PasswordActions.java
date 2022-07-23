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

    public String generatePassword(int n) {
        String alphaNumericString = "ABCDEFGHJKLMNPQRSTUVWXYZ"
                + "23456789"
                + "abcdefghijkmnpqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());

            sb.append(alphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public String generateShortCode(int n) {
        String alphaNumericString ="0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());

            sb.append(alphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
