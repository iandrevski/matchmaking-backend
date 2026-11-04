package sportify.matchmakingbackend.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.enums.NotificationType;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.service.domain.NotificationService;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

// ги претвора настаните во известувања; работи ДУРИ ОТКАКО промената е зачувана во базата
@Component
public class NotificationEventListener {

    private static final DateTimeFormatter WHEN = DateTimeFormatter.ofPattern("EEE d MMM, HH:mm", Locale.ENGLISH);

    private final NotificationService notificationService;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    public NotificationEventListener(NotificationService notificationService, MatchRepository matchRepository,
                                     PlayerRepository playerRepository) {
        this.notificationService = notificationService;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPlayerJoined(MatchEvents.PlayerJoined event) {
        Match match = matchRepository.findById(event.matchId()).orElse(null);
        Player player = playerRepository.findById(event.playerId()).orElse(null);
        if (match == null || player == null || match.getOrganizer() == null
                || match.getOrganizer().getId().equals(player.getId())) {
            return;
        }
        notificationService.notify(match.getOrganizer(), NotificationType.PLAYER_JOINED,
                "👋 New player joined",
                player.getUsername() + " joined your " + describe(match) + ".",
                match);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMatchFull(MatchEvents.MatchFull event) {
        matchRepository.findById(event.matchId()).ifPresent(match ->
                match.getParticipants().forEach(player ->
                        notificationService.notify(player, NotificationType.MATCH_FULL,
                                "✅ Match is full",
                                "Your " + describe(match) + " is full. See you there!",
                                match)));
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMatchCancelled(MatchEvents.MatchCancelled event) {
        matchRepository.findById(event.matchId()).ifPresent(match ->
                match.getParticipants().stream()
                        .filter(player -> !player.getId().equals(event.cancelledById()))
                        .forEach(player -> notificationService.notify(player, NotificationType.MATCH_CANCELLED,
                                "❌ Match cancelled",
                                "The organizer cancelled the " + describe(match) + ".",
                                match)));
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMatchesFinished(MatchEvents.MatchesFinished event) {
        matchRepository.findAllById(event.matchIds()).forEach(match ->
                match.getParticipants().forEach(player ->
                        notificationService.notifyOnce(player, NotificationType.REVIEW_REQUEST,
                                "⭐ How was the match?",
                                "Rate your teammates and the venue from " + describe(match) + ".",
                                match)));
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMessageSent(MatchEvents.MessageSent event) {
        Match match = matchRepository.findById(event.matchId()).orElse(null);
        Player sender = playerRepository.findById(event.senderId()).orElse(null);
        if (match == null || sender == null) {
            return;
        }
        match.getParticipants().stream()
                .filter(player -> !player.getId().equals(sender.getId()))
                .forEach(player -> notificationService.notifyMessage(player, match, sender, event.text()));
    }

    // "Football match at Sport Center Aerodrom (Thu 5 Nov, 18:00)"
    private static String describe(Match match) {
        return match.getSport().getName() + " match at " + match.getVenue().getName()
                + " (" + match.getDateFrom().format(WHEN) + ")";
    }
}