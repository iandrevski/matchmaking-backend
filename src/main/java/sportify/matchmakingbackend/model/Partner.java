package sportify.matchmakingbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportify.matchmakingbackend.model.enums.Role;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partners")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Partner extends User {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venue> venues = new ArrayList<>();

    public Partner(String username, String email, String password) {
        super(username, email, password, Role.ROLE_OWNER);
    }
}
