package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.CreateReportDto;
import sportify.matchmakingbackend.dto.ReportDto;
import sportify.matchmakingbackend.dto.ReportStatusDto;
import sportify.matchmakingbackend.model.enums.ReportStatus;
import sportify.matchmakingbackend.service.application.ReportApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportApplicationService reportApplicationService;

    public ReportController(ReportApplicationService reportApplicationService) {
        this.reportApplicationService = reportApplicationService;
    }

    // играч пријавува играч; одговорот е празен (пријавениот не дознава ништо)
    @PostMapping("/players/{playerId}/reports")
    public ResponseEntity<Void> report(@PathVariable Long playerId, @AuthenticationPrincipal Jwt jwt,
                                       @Valid @RequestBody CreateReportDto dto) {
        reportApplicationService.report(userId(jwt), playerId, dto);
        return ResponseEntity.noContent().build();
    }

    // ---------- само за админ ----------
    @GetMapping("/admin/reports")
    public List<ReportDto> findReports(@RequestParam(defaultValue = "OPEN") ReportStatus status) {
        return reportApplicationService.findByStatus(status);
    }

    @PostMapping("/admin/reports/{reportId}/status")
    public ReportDto changeStatus(@PathVariable Long reportId, @Valid @RequestBody ReportStatusDto dto) {
        return reportApplicationService.changeStatus(reportId, dto.status());
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}