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

    Player addFavoriteVenue(Long playerId, Long venueId);

    Player removeFavoriteVenue(Long playerId, Long venueId);

    Player addFavoriteMatch(Long playerId, Long matchId);

    Player removeFavoriteMatch(Long playerId, Long matchId);
}
