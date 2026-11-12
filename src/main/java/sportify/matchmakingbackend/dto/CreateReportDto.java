package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sportify.matchmakingbackend.model.enums.ReportReason;

public record CreateReportDto(
        @NotNull ReportReason reason,
        @Size(max = 1000) String details,
        Long matchId
) {
}