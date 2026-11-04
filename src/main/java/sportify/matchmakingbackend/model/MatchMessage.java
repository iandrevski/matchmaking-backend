package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// порака во лобито на натпреварот
@Entity
@Table(name = "match_messages", indexes = @Index(columnList = "match_id, id"))
@Getter
@Setter
@NoArgsConstructor
public class MatchMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private Player sender;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public MatchMessage(Match match, Player sender, String text) {
        this.match = match;
        this.sender = sender;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }
}