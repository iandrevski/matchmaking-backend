package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sportify.matchmakingbackend.model.Sport;

public record CreateSportDto(
        @NotBlank String name,
        @NotNull @Positive Integer numberOfPlayers
) {

    public Sport toSport() {
        return new Sport(name, numberOfPlayers);
    }
}
