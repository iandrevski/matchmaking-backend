package sportify.matchmakingbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sportify.matchmakingbackend.model.enums.ReviewTag;

import java.util.List;
import java.util.Set;

// сè што учесникот го оценува за еден натпревар, во едно барање
public record ReviewInputDto(
        @NotNull List<@Valid PlayerInput> players,
        @Valid VenueInput venue
) {

    public record PlayerInput(
            @NotNull Long playerId,
            @Min(1) @Max(5) int stars,
            Set<ReviewTag> tags
    ) {
    }

    public record VenueInput(
            @Min(1) @Max(5) int stars,
            @Size(max = 500) String comment
    ) {
    }
}