package sportify.matchmakingbackend.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sportify.matchmakingbackend.service.domain.MatchService;
import sportify.matchmakingbackend.service.domain.NotificationService;

@Component
public class MatchStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(MatchStatusScheduler.class);

    private final MatchService matchService;
    private final NotificationService notificationService;

    public MatchStatusScheduler(MatchService matchService, NotificationService notificationService) {
        this.matchService = matchService;
        this.notificationService = notificationService;
    }

    // 10 секунди по стартување, па на секоја минута
    @Scheduled(fixedRate = 60_000, initialDelay = 10_000)
    public void finishPastMatches() {
        int finished = matchService.finishPastMatches();
        if (finished > 0) {
            log.info("Marked {} past match(es) as FINISHED", finished);
        }
    }
    // потсетник 2 часа пред натпреварот (секој играч го добива само еднаш)
    @Scheduled(fixedRate = 60_000, initialDelay = 15_000)
    public void sendReminders() {
        int sent = notificationService.sendMatchReminders();
        if (sent > 0) {
            log.info("Sent {} match reminder(s)", sent);
        }
    }
}