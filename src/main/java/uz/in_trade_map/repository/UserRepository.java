package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.in_trade_map.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameAndActiveTrue(String username);

    Optional<User> findByIdAndActiveTrue(UUID id);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByEmailAndActiveTrue(String email);

    List<User> findAllByCompanyIdAndActiveTrue(Integer id);

}
