package sportify.matchmakingbackend.events;

import java.util.List;

// настани што ги објавува domain слојот; известувањата ги слушаат одделно
public final class MatchEvents {

    private MatchEvents() {
    }

    public record PlayerJoined(Long matchId, Long playerId) {
    }

    public record MatchFull(Long matchId) {
    }

    public record MatchCancelled(Long matchId, Long cancelledById) {
    }

    public record MatchesFinished(List<Long> matchIds) {
    }

    public record MessageSent(Long matchId, Long senderId, String text) {
    }
}