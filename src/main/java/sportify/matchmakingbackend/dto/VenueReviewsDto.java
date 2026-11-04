package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.VenueReview;

import java.time.LocalDateTime;
import java.util.List;

public record VenueReviewsDto(Double averageStars, int reviewCount, List<Item> reviews) {

    public record Item(Long id, String author, int stars, String comment, LocalDateTime date) {

        public static Item from(VenueReview review) {
            return new Item(review.getId(), review.getAuthor().getUsername(), review.getStars(),
                    review.getComment(), review.getUpdatedAt());
        }
    }
}