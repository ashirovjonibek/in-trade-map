package uz.in_trade_map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uz.in_trade_map.dtos.ResToken;
import uz.in_trade_map.dtos.SignIn;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.payload.AllApiResponse;
import uz.in_trade_map.repository.UserRepository;
import uz.in_trade_map.secret.JwtTokenProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndActiveTrue(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public User loadByUserId(UUID userId) {
        return userRepository.findByIdAndActiveTrue(userId).orElseThrow(() -> new IllegalStateException("user not found"));
    }

    public ResToken signIn(SignIn signIn) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signIn.getUsername(), signIn.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User principal = (User) authentication.getPrincipal();
            if (principal.getCompany()!=null&&!principal.getCompany().isActive()){
                throw new IllegalStateException("Your company is blocked!");
            }
            String jwt = jwtTokenProvider.generateToken(principal);
            return new ResToken(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
