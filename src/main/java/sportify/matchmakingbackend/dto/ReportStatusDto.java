package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.NotNull;
import sportify.matchmakingbackend.model.enums.ReportStatus;

public record ReportStatusDto(@NotNull ReportStatus status) {
}