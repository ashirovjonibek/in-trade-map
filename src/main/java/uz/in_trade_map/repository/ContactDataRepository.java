package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.ContactData;

public interface ContactDataRepository extends JpaRepository<ContactData,Integer> {
}
