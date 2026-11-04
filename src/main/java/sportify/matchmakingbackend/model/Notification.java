package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.NotificationType;

import java.time.LocalDateTime;

// известување за еден корисник; readAt == null значи непрочитано
@Entity
@Table(name = "notifications", indexes = @Index(columnList = "recipient_id, read_at"))
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 300)
    private String body;

    // натпреварот на кој се однесува (клик води таму)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    // колку настани се групирани во ова известување (пораки во чат)
    @Column(nullable = false)
    private int groupCount = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public Notification(User recipient, NotificationType type, String title, String body, Match match) {
        this.recipient = recipient;
        this.type = type;
        this.title = title;
        this.body = body;
        this.match = match;
        this.createdAt = LocalDateTime.now();
    }
}