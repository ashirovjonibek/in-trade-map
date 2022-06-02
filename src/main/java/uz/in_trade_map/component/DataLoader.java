package uz.in_trade_map.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.User;
import uz.in_trade_map.entity.enums.RoleName;
import uz.in_trade_map.repository.RoleRepository;
import uz.in_trade_map.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.sql.init.mode}")
    String mode;


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(mode.equals("always")){
            Role admin = roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            userRepository.save(new User(
                    "Admin",
                    "Admin",
                    "+998912345678",
                    "admin123",
                    passwordEncoder.encode("admin123"),
                    Collections.singleton(admin)
            ));
        }
    }
}
