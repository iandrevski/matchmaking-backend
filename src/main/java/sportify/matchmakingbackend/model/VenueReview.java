package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// оценка за теренот од учесник во натпревар
@Entity
@Table(
        name = "venue_reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "author_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class VenueReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Player author;

    @Column(nullable = false)
    private int stars;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public VenueReview(Match match, Venue venue, Player author) {
        this.match = match;
        this.venue = venue;
        this.author = author;
    }
}