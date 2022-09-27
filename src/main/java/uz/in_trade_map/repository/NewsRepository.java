package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.News;

public interface NewsRepository extends JpaRepository<News,Integer> {
}
