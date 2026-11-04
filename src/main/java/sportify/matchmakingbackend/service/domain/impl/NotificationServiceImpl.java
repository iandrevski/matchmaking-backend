package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Notification;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.User;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.enums.NotificationType;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.NotificationRepository;
import sportify.matchmakingbackend.service.domain.NotificationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final int REMINDER_HOURS = 2;
    private static final int PREVIEW_LENGTH = 120;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private final NotificationRepository notificationRepository;
    private final MatchRepository matchRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, MatchRepository matchRepository) {
        this.notificationRepository = notificationRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public void notify(User recipient, NotificationType type, String title, String body, Match match) {
        notificationRepository.save(new Notification(recipient, type, title, body, match));
    }

    @Override
    @Transactional
    public void notifyOnce(User recipient, NotificationType type, String title, String body, Match match) {
        if (!notificationRepository.existsByRecipientIdAndMatchIdAndType(recipient.getId(), match.getId(), type)) {
            notify(recipient, type, title, body, match);
        }
    }

    @Override
    @Transactional
    public void notifyMessage(Player recipient, Match match, Player sender, String text) {
        String preview = sender.getUsername() + ": " + shorten(text);
        Notification notification = notificationRepository
                .findFirstByRecipientIdAndMatchIdAndTypeAndReadAtIsNull(recipient.getId(), match.getId(), NotificationType.NEW_MESSAGE)
                .orElse(null);

        if (notification == null) {
            notify(recipient, NotificationType.NEW_MESSAGE,
                    "💬 New message · " + match.getSport().getName() + " lobby", preview, match);
            return;
        }
        // уште непрочитано → истото известување се ажурира и оди најгоре
        notification.setGroupCount(notification.getGroupCount() + 1);
        notification.setTitle("💬 " + notification.getGroupCount() + " new messages · "
                + match.getSport().getName() + " lobby");
        notification.setBody(preview);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public int sendMatchReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Match> soon = matchRepository.findByDateFromBetweenAndStatusIn(
                now, now.plusHours(REMINDER_HOURS), List.of(MatchStatus.OPEN, MatchStatus.FULL));

        int sent = 0;
        for (Match match : soon) {
            for (Player player : match.getParticipants()) {
                if (!notificationRepository.existsByRecipientIdAndMatchIdAndType(
                        player.getId(), match.getId(), NotificationType.MATCH_REMINDER)) {
                    notify(player, NotificationType.MATCH_REMINDER, "⏰ Starting soon",
                            match.getSport().getName() + " at " + match.getVenue().getName()
                                    + " starts at " + match.getDateFrom().format(TIME) + ".",
                            match);
                    sent++;
                }
            }
        }
        return sent;
    }

    @Override
    public List<Notification> findLatest(Long userId) {
        return notificationRepository.findTop50ByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public long countUnread(Long userId) {
        return notificationRepository.countByRecipientIdAndReadAtIsNull(userId);
    }

    @Override
    @Transactional
    public Notification markRead(Long notificationId, Long userId) {
        // туѓо известување = „не постои“, за да не се открива дека постои
        Notification notification = notificationRepository.findByIdAndRecipientId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public int markAllRead(Long userId) {
        return notificationRepository.markAllRead(userId, LocalDateTime.now());
    }

    private static String shorten(String text) {
        return text.length() <= PREVIEW_LENGTH ? text : text.substring(0, PREVIEW_LENGTH - 1) + "…";
    }
}