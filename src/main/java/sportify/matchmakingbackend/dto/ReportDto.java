package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.PlayerReport;
import sportify.matchmakingbackend.model.enums.ReportReason;
import sportify.matchmakingbackend.model.enums.ReportStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.ToLongFunction;

// за админот: целосна пријава
public record ReportDto(
        Long id,
        String reporter,
        Long reportedId,
        String reported,
        Long matchId,
        ReportReason reason,
        String details,
        ReportStatus status,
        long openReportsAgainstPlayer,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {

    public static ReportDto from(PlayerReport report, long openReportsAgainstPlayer) {
        return new ReportDto(
                report.getId(),
                report.getReporter().getUsername(),
                report.getReported().getId(),
                report.getReported().getUsername(),
                report.getMatch() != null ? report.getMatch().getId() : null,
                report.getReason(),
                report.getDetails(),
                report.getStatus(),
                openReportsAgainstPlayer,
                report.getCreatedAt(),
                report.getReviewedAt()
        );
    }

    public static List<ReportDto> from(List<PlayerReport> reports, ToLongFunction<PlayerReport> openCount) {
        return reports.stream().map(r -> from(r, openCount.applyAsLong(r))).toList();
    }
}