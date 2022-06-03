package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Quarter;

import java.util.List;

public interface QuarterRepository extends JpaRepository<Quarter, Integer> {
    List<Quarter> findAllByDistrictId(Integer id);
}
