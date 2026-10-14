package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;
    private String municipality;
    private String address;

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id")
    private Partner owner;

    // кои спортови може да се играат на овој објект
    @ManyToMany
    @JoinTable(
            name = "venue_sports",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    private Set<Sport> sports = new HashSet<>();

    public Venue(String name, String city, String municipality, String address, Partner owner) {
        this.name = name;
        this.city = city;
        this.municipality = municipality;
        this.address = address;
        this.owner = owner;
    }
}
