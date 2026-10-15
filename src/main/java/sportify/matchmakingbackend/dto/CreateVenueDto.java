package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateVenueDto(
        @NotBlank String name,
        String city,
        String municipality,
        String address,
        Double latitude,
        Double longitude,
        @NotEmpty List<Long> sportIds
) {
}
