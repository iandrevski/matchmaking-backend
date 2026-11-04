package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.Notification;
import sportify.matchmakingbackend.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop50ByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    long countByRecipientIdAndReadAtIsNull(Long recipientId);

    Optional<Notification> findByIdAndRecipientId(Long id, Long recipientId);

    // непрочитано известување за пораки од истиот чат → се групира наместо ново
    Optional<Notification> findFirstByRecipientIdAndMatchIdAndTypeAndReadAtIsNull(
            Long recipientId, Long matchId, NotificationType type);

    // за известувањата што се праќаат само еднаш по натпревар (потсетник, оценување)
    boolean existsByRecipientIdAndMatchIdAndType(Long recipientId, Long matchId, NotificationType type);

    @Modifying
    @Query("update Notification n set n.readAt = :now where n.recipient.id = :recipientId and n.readAt is null")
    int markAllRead(@Param("recipientId") Long recipientId, @Param("now") LocalDateTime now);
}