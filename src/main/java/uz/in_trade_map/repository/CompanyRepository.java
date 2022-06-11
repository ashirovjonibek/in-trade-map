package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer>, JpaSpecificationExecutor<Company> {
}
