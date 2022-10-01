package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Banner;

import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    Optional<Banner> findByIdAndActiveTrue(Integer id);

    Page<Banner> findAllByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
