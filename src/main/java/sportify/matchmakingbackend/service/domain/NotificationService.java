package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Notification;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.User;
import sportify.matchmakingbackend.model.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    void notify(User recipient, NotificationType type, String title, String body, Match match);

    // само ако тој корисник уште нема такво известување за тој натпревар
    void notifyOnce(User recipient, NotificationType type, String title, String body, Match match);

    // пораките во чат се групираат во едно непрочитано известување
    void notifyMessage(Player recipient, Match match, Player sender, String text);

    // потсетник за натпреварите што почнуваат во следните 2 часа (го вика scheduler-от)
    int sendMatchReminders();

    List<Notification> findLatest(Long userId);

    long countUnread(Long userId);

    Notification markRead(Long notificationId, Long userId);

    int markAllRead(Long userId);
}