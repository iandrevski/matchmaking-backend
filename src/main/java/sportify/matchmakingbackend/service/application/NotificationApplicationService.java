package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.NotificationDto;
import sportify.matchmakingbackend.dto.UnreadCountDto;

import java.util.List;

public interface NotificationApplicationService {

    List<NotificationDto> findLatest(Long userId);

    UnreadCountDto countUnread(Long userId);

    NotificationDto markRead(Long notificationId, Long userId);

    UnreadCountDto markAllRead(Long userId);
}