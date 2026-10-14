package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer numberOfPlayers; // вкупно играчи за еден натпревар

    public Sport(String name, Integer numberOfPlayers) {
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }
}

