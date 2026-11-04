package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.dto.ReviewInputDto;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.PlayerReview;
import sportify.matchmakingbackend.model.VenueReview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewService {

    // крајот на рокот за оценување на натпреварот
    LocalDateTime reviewableUntil(Match match);

    boolean canReview(Match match, Long playerId);

    void submit(Long matchId, Long reviewerId, ReviewInputDto input);

    List<PlayerReview> findGivenPlayerReviews(Long matchId, Long reviewerId);

    Optional<VenueReview> findGivenVenueReview(Long matchId, Long reviewerId);

    List<PlayerReview> findReceived(Long playerId);

    List<VenueReview> findVenueReviews(Long venueId);
}