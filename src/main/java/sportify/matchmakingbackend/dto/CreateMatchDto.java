package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMatchDto(
        @NotNull @Future LocalDateTime dateFrom,
        @NotNull @Future LocalDateTime dateTo,
        @NotNull Long sportId,
        @NotNull Long venueId
) {
}
