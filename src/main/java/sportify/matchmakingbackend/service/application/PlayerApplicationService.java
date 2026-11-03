package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.FavoritesDto;
import sportify.matchmakingbackend.dto.LocationDto;
import sportify.matchmakingbackend.dto.PlayerDto;
import sportify.matchmakingbackend.dto.UpdatePlayerDto;

import java.util.List;
import java.util.Optional;

public interface PlayerApplicationService {

    List<PlayerDto> findAll();

    Optional<PlayerDto> findById(Long id);

    Optional<PlayerDto> findByUsername(String username);

    PlayerDto updateProfile(Long id, UpdatePlayerDto dto);

    PlayerDto updateLocation(Long id, LocationDto dto);

    FavoritesDto getFavorites(Long playerId);

    FavoritesDto addFavoriteVenue(Long playerId, Long venueId);

    FavoritesDto removeFavoriteVenue(Long playerId, Long venueId);

    FavoritesDto addFavoriteMatch(Long playerId, Long matchId);

    FavoritesDto removeFavoriteMatch(Long playerId, Long matchId);
}
