package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.NotificationDto;
import sportify.matchmakingbackend.dto.UnreadCountDto;
import sportify.matchmakingbackend.service.application.NotificationApplicationService;
import sportify.matchmakingbackend.service.domain.NotificationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationApplicationServiceImpl implements NotificationApplicationService {

    private final NotificationService notificationService;

    public NotificationApplicationServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public List<NotificationDto> findLatest(Long userId) {
        return NotificationDto.from(notificationService.findLatest(userId));
    }

    @Override
    public UnreadCountDto countUnread(Long userId) {
        return new UnreadCountDto(notificationService.countUnread(userId));
    }

    @Override
    @Transactional
    public NotificationDto markRead(Long notificationId, Long userId) {
        return NotificationDto.from(notificationService.markRead(notificationId, userId));
    }

    @Override
    @Transactional
    public UnreadCountDto markAllRead(Long userId) {
        notificationService.markAllRead(userId);
        return new UnreadCountDto(0);
    }
}