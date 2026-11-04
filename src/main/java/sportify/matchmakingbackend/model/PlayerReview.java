package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.ReviewTag;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// оценка што еден играч му ја дава на друг по еден натпревар
@Entity
@Table(
        name = "player_reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "reviewer_id", "reviewed_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class PlayerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id")
    private Player reviewer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewed_id")
    private Player reviewed;

    @Column(nullable = false)
    private int stars;

    @ElementCollection
    @CollectionTable(name = "player_review_tags", joinColumns = @JoinColumn(name = "review_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false, length = 30)
    private Set<ReviewTag> tags = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public PlayerReview(Match match, Player reviewer, Player reviewed) {
        this.match = match;
        this.reviewer = reviewer;
        this.reviewed = reviewed;
    }
}