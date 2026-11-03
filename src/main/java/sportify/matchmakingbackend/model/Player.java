package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.Role;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Player extends User {

    private Integer age;
    private Double height; // cm
    private Double weight; // kg

    private Double homeLat;
    private Double homeLong;

    private Double currLat;
    private Double currLong;

    @ManyToMany(mappedBy = "participants")
    private Set<Match> matches = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "player_favorite_venues",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "venue_id")
    )
    private Set<Venue> favoriteVenues = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "player_favorite_matches",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private Set<Match> favoriteMatches = new HashSet<>();

    public Player(String username, String email, String password) {
        super(username, email, password, Role.ROLE_USER);
    }
}
