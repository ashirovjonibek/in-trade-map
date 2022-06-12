package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Attachment;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    Optional<Attachment> findByIdAndActiveTrue(UUID id);
}
