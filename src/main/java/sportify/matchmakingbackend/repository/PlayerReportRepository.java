package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.PlayerReport;
import sportify.matchmakingbackend.model.enums.ReportStatus;

import java.util.List;

@Repository
public interface PlayerReportRepository extends JpaRepository<PlayerReport, Long> {

    boolean existsByReporterIdAndReportedIdAndStatus(Long reporterId, Long reportedId, ReportStatus status);

    List<PlayerReport> findByStatusOrderByCreatedAtDesc(ReportStatus status);

    // колку различни играчи го пријавиле (за админот, да ги види најсериозните прво)
    List<PlayerReport> findByReportedIdAndStatus(Long reportedId, ReportStatus status);
}