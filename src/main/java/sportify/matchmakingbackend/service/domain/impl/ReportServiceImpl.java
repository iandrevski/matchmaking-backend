package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.PlayerReport;
import sportify.matchmakingbackend.model.enums.ReportReason;
import sportify.matchmakingbackend.model.enums.ReportStatus;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerReportRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.service.domain.ReportService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final PlayerReportRepository reportRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    public ReportServiceImpl(PlayerReportRepository reportRepository, PlayerRepository playerRepository,
                             MatchRepository matchRepository) {
        this.reportRepository = reportRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public PlayerReport report(Long reporterId, Long reportedId, ReportReason reason, String details, Long matchId) {
        if (reporterId.equals(reportedId)) {
            throw new InvalidOperationException("You cannot report yourself");
        }
        String cleanDetails = details == null || details.isBlank() ? null : details.trim();
        if (reason == ReportReason.OTHER && cleanDetails == null) {
            throw new InvalidOperationException("Please describe what happened");
        }

        Player reporter = playerRepository.findById(reporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", reporterId));
        Player reported = playerRepository.findById(reportedId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", reportedId));

        Match match = null;
        if (matchId != null) {
            match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));
            if (!isParticipant(match, reporterId) || !isParticipant(match, reportedId)) {
                throw new InvalidOperationException("Both players must be in this match");
            }
        } else if (!matchRepository.playedTogether(reporterId, reportedId)) {
            // пријава само за некој со кого си играл, за да нема спам од непознати
            throw new ForbiddenOperationException("You can only report players you have played with");
        }

        if (reportRepository.existsByReporterIdAndReportedIdAndStatus(reporterId, reportedId, ReportStatus.OPEN)) {
            throw new InvalidOperationException("You already reported this player. We're reviewing it.");
        }
        return reportRepository.save(new PlayerReport(reporter, reported, match, reason, cleanDetails));
    }

    @Override
    public List<PlayerReport> findByStatus(ReportStatus status) {
        return reportRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    public long countOpenAgainst(Long playerId) {
        return reportRepository.findByReportedIdAndStatus(playerId, ReportStatus.OPEN).stream()
                .map(r -> r.getReporter().getId())
                .distinct()
                .count();
    }

    @Override
    @Transactional
    public PlayerReport changeStatus(Long reportId, ReportStatus status) {
        PlayerReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", reportId));
        report.setStatus(status);
        report.setReviewedAt(status == ReportStatus.OPEN ? null : LocalDateTime.now());
        return reportRepository.save(report);
    }

    private static boolean isParticipant(Match match, Long playerId) {
        return match.getParticipants().stream().anyMatch(p -> p.getId().equals(playerId));
    }
}