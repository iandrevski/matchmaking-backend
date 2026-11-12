package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.ReportReason;
import sportify.matchmakingbackend.model.enums.ReportStatus;

import java.time.LocalDateTime;

// пријава за недолично однесување; ја гледа само админ
@Entity
@Table(name = "player_reports", indexes = @Index(columnList = "status, created_at"))
@Getter
@Setter
@NoArgsConstructor
public class PlayerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_id")
    private Player reporter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_id")
    private Player reported;

    // натпреварот каде што се случило (опционално)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportReason reason;

    @Column(length = 1000)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status = ReportStatus.OPEN;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    public PlayerReport(Player reporter, Player reported, Match match, ReportReason reason, String details) {
        this.reporter = reporter;
        this.reported = reported;
        this.match = match;
        this.reason = reason;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }
}