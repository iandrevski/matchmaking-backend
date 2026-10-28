package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Partner;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Player registerPlayer(String username, String email, String rawPassword);

    Partner registerPartner(String username, String email, String rawPassword);

    User authenticate(String username, String rawPassword);
}