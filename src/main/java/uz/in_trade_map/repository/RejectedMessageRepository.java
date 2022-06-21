package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.RejectedMessage;

import java.util.List;
import java.util.Optional;

public interface RejectedMessageRepository extends JpaRepository<RejectedMessage, Integer> {
    List<RejectedMessage> findAllByActiveTrueAndTableNameAndTableRowId(String tableName, String tableRowId);

    Optional<RejectedMessage> findByActiveTrueAndTableNameAndTableRowId(String tableName, String tableRowId);
}
