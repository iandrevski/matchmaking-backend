package sportify.matchmakingbackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateFrom;

    @Column(nullable = false)
    private LocalDateTime dateTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private Player organizer;

    @ManyToMany
    @JoinTable(
            name = "match_participants",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> participants = new HashSet<>();

    public Match(LocalDateTime dateFrom, LocalDateTime dateTo, Sport sport, Venue venue, Player organizer) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.sport = sport;
        this.venue = venue;
        this.organizer = organizer;
    }

    public boolean isFull() {
        return participants.size() >= sport.getNumberOfPlayers();
    }
}

