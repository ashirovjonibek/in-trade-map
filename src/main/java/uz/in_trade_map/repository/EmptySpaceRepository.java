package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.EmptySpace;

import java.util.Optional;

public interface EmptySpaceRepository extends JpaRepository<EmptySpace, Integer>, JpaSpecificationExecutor<EmptySpace> {
    Optional<EmptySpace> findByIdAndActiveTrue(Integer id);
}
