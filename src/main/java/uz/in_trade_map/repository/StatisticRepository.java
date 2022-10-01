package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Statistic;

import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
    Optional<Statistic> findByIdAndActiveTrue(Integer id);

    Page<Statistic> findAllByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
