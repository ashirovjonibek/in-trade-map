package uz.in_trade_map.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByIdAndActiveTrue(Integer id);

    Page<Category> findAllByActiveTrue(Pageable pageable);

    List<Category> findAllByCategoryIdAndActiveTrue(Integer id);

    Page<Category> findAllByCategoryIdAndActiveTrue(Integer id, Pageable pageable);

    Page<Category> findAllByCategoryNullAndActiveTrue(Pageable pageable);
}
