package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.CreateReportDto;
import sportify.matchmakingbackend.dto.ReportDto;
import sportify.matchmakingbackend.model.PlayerReport;
import sportify.matchmakingbackend.model.enums.ReportStatus;
import sportify.matchmakingbackend.service.application.ReportApplicationService;
import sportify.matchmakingbackend.service.domain.ReportService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportApplicationServiceImpl implements ReportApplicationService {

    private final ReportService reportService;

    public ReportApplicationServiceImpl(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    @Transactional
    public void report(Long reporterId, Long reportedId, CreateReportDto dto) {
        reportService.report(reporterId, reportedId, dto.reason(), dto.details(), dto.matchId());
    }

    @Override
    public List<ReportDto> findByStatus(ReportStatus status) {
        return ReportDto.from(reportService.findByStatus(status),
                report -> reportService.countOpenAgainst(report.getReported().getId()));
    }

    @Override
    @Transactional
    public ReportDto changeStatus(Long reportId, ReportStatus status) {
        PlayerReport report = reportService.changeStatus(reportId, status);
        return ReportDto.from(report, reportService.countOpenAgainst(report.getReported().getId()));
    }
}