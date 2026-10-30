package sportify.matchmakingbackend.dto;

public record NearbyMatchDto(MatchDto match, double distanceKm) {
}