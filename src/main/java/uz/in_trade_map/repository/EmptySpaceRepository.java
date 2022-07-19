package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.in_trade_map.entity.EmptySpace;

import java.util.Optional;

public interface EmptySpaceRepository extends JpaRepository<EmptySpace, Integer>, JpaSpecificationExecutor<EmptySpace> {
    Optional<EmptySpace> findByIdAndActiveTrue(Integer id);

    @Query(nativeQuery = true,value = "select max(empty_space.starting_price) from empty_space")
    Float maxStartingPrice();

    @Query(nativeQuery = true,value = "select min(empty_space.starting_price) from empty_space")
    Float minStartingPrice();

    @Query(nativeQuery = true,value = "select max(empty_space.annual_price) from empty_space")
    Float maxAnnualPrice();

    @Query(nativeQuery = true,value = "select min(empty_space.annual_price) from empty_space")
    Float minAnnualPrice();
}
