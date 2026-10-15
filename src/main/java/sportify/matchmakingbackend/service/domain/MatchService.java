package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    List<Match> findAll();

    Optional<Match> findById(Long id);

    List<Match> findByStatus(MatchStatus status);

    List<Match> findOpenBySport(Long sportId);

    List<Match> findByVenue(Long venueId);

    List<Match> findByPlayer(Long playerId);

    Match create(Match match);

    Match join(Long matchId, Long playerId);

    Match leave(Long matchId, Long playerId);

    Match cancel(Long matchId);
}
