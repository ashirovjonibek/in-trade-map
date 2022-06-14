package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
}
