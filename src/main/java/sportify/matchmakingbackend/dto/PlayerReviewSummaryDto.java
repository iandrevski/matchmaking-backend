package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.enums.ReviewTag;

import java.util.List;

// анонимен збир: просек, ознаки и број натпревари, без кој што дал
public record PlayerReviewSummaryDto(
        Long playerId,
        String username,
        int reviewCount,
        Double averageStars,
        List<TagCount> tags,
        int matchesPlayed
) {

    public record TagCount(ReviewTag tag, long count) {
    }
}