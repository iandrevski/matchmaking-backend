package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageDto(@NotBlank @Size(max = 500) String text) {
}