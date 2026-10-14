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

    public Player(String username, String email, String password) {
        super(username, email, password, Role.ROLE_USER);
    }
}
