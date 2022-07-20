package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.in_trade_map.entity.Product;
import uz.in_trade_map.utils.projections.MaxMinProjectionForEmptySpace;
import uz.in_trade_map.utils.projections.MaxMinProjectionForProduct;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndActiveTrue(UUID id);

    @Query(nativeQuery = true,value = "select " +
            "max(priceuzs) as maxPriceUZS, " +
            "min(priceuzs) as minPriceUZS, " +
            "max(priceusd) as maxPriceUSD, " +
            "min(priceusd) as minPriceUSD, " +
            "max(export_priceuzs) as maxExportPriceUZS," +
            "min(export_priceuzs) as minExportPriceUZS," +
            "max(export_priceusd) as maxExportPriceUSD," +
            "min(export_priceusd) as minExportPriceUSD" +
            " from product")
    MaxMinProjectionForProduct maxMinPrices();
}
