package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Application;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Integer>, JpaSpecificationExecutor<Application> {
    Optional<Application> findByIdAndActiveTrue(Integer id);

    Optional<Application> findByIdAndActiveTrueAndIsConfirm(Integer id, Integer status);

    boolean existsByBossPhoneAndActiveTrue(String phone);
}
