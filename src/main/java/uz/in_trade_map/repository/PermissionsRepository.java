package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Permissions;

import java.util.List;
import java.util.Set;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
    List<Permissions> findAllByNameIn(Set<String> roleNames);
}
