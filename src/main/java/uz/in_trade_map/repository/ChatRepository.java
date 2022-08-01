package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.in_trade_map.entity.Chat;
import uz.in_trade_map.entity.User;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer>, JpaSpecificationExecutor<Chat> {
    List<Chat> findAllByActiveTrueAndMembersIn(List<User> members);
}
