package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.UserPasswords;

import java.util.Optional;
import java.util.UUID;

public interface UserPasswordsRepository extends JpaRepository<UserPasswords, UUID> {
    Optional<UserPasswords> findByUserId(UUID id);
}
