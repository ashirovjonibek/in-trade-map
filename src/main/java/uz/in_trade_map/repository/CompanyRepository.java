package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {
    Optional<Company> findByIdAndActiveTrue(Integer id);

    boolean existsByBrandNameAndActiveTrue(String brandName);

    Optional<Company> findByBrandNameAndActiveTrue(String brandName);

    boolean existsByInnAndActiveTrue(String inn);

    Optional<Company> findByInnAndActiveTrue(String inn);
}
