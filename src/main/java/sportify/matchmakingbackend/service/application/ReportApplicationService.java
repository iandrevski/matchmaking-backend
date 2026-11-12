package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.CreateReportDto;
import sportify.matchmakingbackend.dto.ReportDto;
import sportify.matchmakingbackend.model.enums.ReportStatus;

import java.util.List;

public interface ReportApplicationService {

    void report(Long reporterId, Long reportedId, CreateReportDto dto);

    List<ReportDto> findByStatus(ReportStatus status);

    ReportDto changeStatus(Long reportId, ReportStatus status);
}