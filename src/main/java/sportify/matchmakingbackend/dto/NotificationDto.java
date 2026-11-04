package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Notification;
import sportify.matchmakingbackend.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public record NotificationDto(
        Long id,
        NotificationType type,
        String title,
        String body,
        Long matchId,
        int count,
        boolean read,
        LocalDateTime createdAt
) {

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getMatch() != null ? notification.getMatch().getId() : null,
                notification.getGroupCount(),
                notification.getReadAt() != null,
                notification.getCreatedAt()
        );
    }

    public static List<NotificationDto> from(List<Notification> notifications) {
        return notifications.stream().map(NotificationDto::from).toList();
    }
}