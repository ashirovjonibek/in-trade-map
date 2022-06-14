package uz.in_trade_map.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleNameAndActiveTrue(String roleName);

    boolean existsByRoleNameAndActiveTrue(String roleName);
}
