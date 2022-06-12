package uz.in_trade_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.AttachmentContent;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, UUID> {
    Optional<AttachmentContent> findByAttachmentId(UUID id);
}
