package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.MyMatchReviewsDto;
import sportify.matchmakingbackend.dto.PlayerReviewSummaryDto;
import sportify.matchmakingbackend.dto.ReviewInputDto;
import sportify.matchmakingbackend.dto.VenueReviewsDto;
import sportify.matchmakingbackend.service.application.ReviewApplicationService;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewApplicationService reviewApplicationService;

    public ReviewController(ReviewApplicationService reviewApplicationService) {
        this.reviewApplicationService = reviewApplicationService;
    }

    // што веќе оценив во овој натпревар и дали уште можам
    @GetMapping("/matches/{matchId}/reviews/mine")
    public MyMatchReviewsDto getMine(@PathVariable Long matchId, @AuthenticationPrincipal Jwt jwt) {
        return reviewApplicationService.getMine(matchId, userId(jwt));
    }

    // нови оценки или промена на постоечките (во рокот од 7 дена)
    @PutMapping("/matches/{matchId}/reviews")
    public MyMatchReviewsDto submit(@PathVariable Long matchId, @AuthenticationPrincipal Jwt jwt,
                                    @Valid @RequestBody ReviewInputDto input) {
        return reviewApplicationService.submit(matchId, userId(jwt), input);
    }

    @GetMapping("/players/{playerId}/reviews/summary")
    public PlayerReviewSummaryDto getPlayerSummary(@PathVariable Long playerId) {
        return reviewApplicationService.getPlayerSummary(playerId);
    }

    @GetMapping("/venues/{venueId}/reviews")
    public VenueReviewsDto getVenueReviews(@PathVariable Long venueId) {
        return reviewApplicationService.getVenueReviews(venueId);
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}