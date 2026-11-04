package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.PlayerReview;
import sportify.matchmakingbackend.model.VenueReview;
import sportify.matchmakingbackend.model.enums.ReviewTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

// што веќе оценил најавениот играч во овој натпревар (за да се пополни формата)
public record MyMatchReviewsDto(
        boolean canReview,
        LocalDateTime reviewableUntil,
        List<GivenPlayerReview> players,
        GivenVenueReview venue
) {

    public record GivenPlayerReview(Long playerId, int stars, Set<ReviewTag> tags) {

        public static GivenPlayerReview from(PlayerReview review) {
            return new GivenPlayerReview(review.getReviewed().getId(), review.getStars(), Set.copyOf(review.getTags()));
        }
    }

    public record GivenVenueReview(int stars, String comment) {

        public static GivenVenueReview from(VenueReview review) {
            return new GivenVenueReview(review.getStars(), review.getComment());
        }
    }
}