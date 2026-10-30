package sportify.matchmakingbackend.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sportify.matchmakingbackend.service.domain.MatchService;

@Component
public class MatchStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(MatchStatusScheduler.class);

    private final MatchService matchService;

    public MatchStatusScheduler(MatchService matchService) {
        this.matchService = matchService;
    }

    // 10 секунди по стартување, па на секоја минута
    @Scheduled(fixedRate = 60_000, initialDelay = 10_000)
    public void finishPastMatches() {
        int finished = matchService.finishPastMatches();
        if (finished > 0) {
            log.info("Marked {} past match(es) as FINISHED", finished);
        }
    }
}