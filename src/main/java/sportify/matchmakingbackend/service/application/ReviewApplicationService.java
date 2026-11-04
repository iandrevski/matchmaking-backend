package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.MyMatchReviewsDto;
import sportify.matchmakingbackend.dto.PlayerReviewSummaryDto;
import sportify.matchmakingbackend.dto.ReviewInputDto;
import sportify.matchmakingbackend.dto.VenueReviewsDto;

public interface ReviewApplicationService {

    MyMatchReviewsDto getMine(Long matchId, Long playerId);

    MyMatchReviewsDto submit(Long matchId, Long playerId, ReviewInputDto input);

    PlayerReviewSummaryDto getPlayerSummary(Long playerId);

    VenueReviewsDto getVenueReviews(Long venueId);
}