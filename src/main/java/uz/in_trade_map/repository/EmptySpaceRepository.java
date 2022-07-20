package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.in_trade_map.entity.EmptySpace;
import uz.in_trade_map.utils.projections.MaxMinProjectionForEmptySpace;

import java.util.Optional;

public interface EmptySpaceRepository extends JpaRepository<EmptySpace, Integer>, JpaSpecificationExecutor<EmptySpace> {
    Optional<EmptySpace> findByIdAndActiveTrue(Integer id);

    @Query(nativeQuery = true,value = "select " +
            "max(empty_space.starting_price) as maxStartingPrice, " +
            "min(empty_space.starting_price) as minStartingPrice, " +
            "max(empty_space.annual_price) as maxAnnualPrice," +
            "min(empty_space.annual_price) as minAnnualPrice" +
            " from empty_space")
    MaxMinProjectionForEmptySpace maxMinPrices();
}
