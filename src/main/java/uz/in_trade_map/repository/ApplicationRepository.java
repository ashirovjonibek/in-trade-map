package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}
