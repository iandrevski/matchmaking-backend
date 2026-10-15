package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    List<Player> findAll();

    Optional<Player> findById(Long id);

    Optional<Player> findByUsername(String username);

    Player updateProfile(Long id, Player player);

    Player updateLocation(Long id, Double lat, Double lng);
}
