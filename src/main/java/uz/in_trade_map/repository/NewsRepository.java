package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News,Integer> {
    Optional<News> findByIdAndActiveTrue(Integer id);

    Page<News> findAllByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
