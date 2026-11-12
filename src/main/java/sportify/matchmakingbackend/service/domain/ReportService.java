package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.PlayerReport;
import sportify.matchmakingbackend.model.enums.ReportReason;
import sportify.matchmakingbackend.model.enums.ReportStatus;

import java.util.List;

public interface ReportService {

    PlayerReport report(Long reporterId, Long reportedId, ReportReason reason, String details, Long matchId);

    List<PlayerReport> findByStatus(ReportStatus status);

    long countOpenAgainst(Long playerId);

    PlayerReport changeStatus(Long reportId, ReportStatus status);
}