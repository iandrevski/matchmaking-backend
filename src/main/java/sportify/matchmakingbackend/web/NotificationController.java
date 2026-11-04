package sportify.matchmakingbackend.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.NotificationDto;
import sportify.matchmakingbackend.dto.UnreadCountDto;
import sportify.matchmakingbackend.service.application.NotificationApplicationService;

import java.util.List;

// сите повици се за најавениот корисник (/api/** без јавно правило бара најава)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationApplicationService notificationApplicationService;

    public NotificationController(NotificationApplicationService notificationApplicationService) {
        this.notificationApplicationService = notificationApplicationService;
    }

    @GetMapping
    public List<NotificationDto> findLatest(@AuthenticationPrincipal Jwt jwt) {
        return notificationApplicationService.findLatest(userId(jwt));
    }

    // мал повик за бројот на ѕвончето (апликацијата го вика често)
    @GetMapping("/unread-count")
    public UnreadCountDto countUnread(@AuthenticationPrincipal Jwt jwt) {
        return notificationApplicationService.countUnread(userId(jwt));
    }

    @PostMapping("/{id}/read")
    public NotificationDto markRead(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return notificationApplicationService.markRead(id, userId(jwt));
    }

    @PostMapping("/read-all")
    public UnreadCountDto markAllRead(@AuthenticationPrincipal Jwt jwt) {
        return notificationApplicationService.markAllRead(userId(jwt));
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}