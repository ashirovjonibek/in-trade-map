package uz.in_trade_map.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.enums.RoleName;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleNameAndActiveTrue(String roleName);

    Set<Role> findAllByRoleNameIn(Set<String> roleNames);

    boolean existsByRoleNameAndActiveTrue(String roleName);
}
