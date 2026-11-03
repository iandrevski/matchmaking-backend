package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.FavoritesDto;
import sportify.matchmakingbackend.dto.LocationDto;
import sportify.matchmakingbackend.dto.PlayerDto;
import sportify.matchmakingbackend.dto.UpdatePlayerDto;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.service.application.PlayerApplicationService;
import sportify.matchmakingbackend.service.domain.PlayerService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlayerApplicationServiceImpl implements PlayerApplicationService {

    private final PlayerService playerService;

    public PlayerApplicationServiceImpl(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public List<PlayerDto> findAll() {
        return PlayerDto.from(playerService.findAll());
    }

    @Override
    public Optional<PlayerDto> findById(Long id) {
        return playerService.findById(id).map(PlayerDto::from);
    }

    @Override
    public Optional<PlayerDto> findByUsername(String username) {
        return playerService.findByUsername(username).map(PlayerDto::from);
    }

    @Override
    @Transactional
    public PlayerDto updateProfile(Long id, UpdatePlayerDto dto) {
        return PlayerDto.from(playerService.updateProfile(id, dto.toPlayer()));
    }

    @Override
    @Transactional
    public PlayerDto updateLocation(Long id, LocationDto dto) {
        return PlayerDto.from(playerService.updateLocation(id, dto.lat(), dto.lng()));
    }

    @Override
    public FavoritesDto getFavorites(Long playerId) {
        return FavoritesDto.from(playerService.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", playerId)));
    }

    @Override
    @Transactional
    public FavoritesDto addFavoriteVenue(Long playerId, Long venueId) {
        return FavoritesDto.from(playerService.addFavoriteVenue(playerId, venueId));
    }

    @Override
    @Transactional
    public FavoritesDto removeFavoriteVenue(Long playerId, Long venueId) {
        return FavoritesDto.from(playerService.removeFavoriteVenue(playerId, venueId));
    }

    @Override
    @Transactional
    public FavoritesDto addFavoriteMatch(Long playerId, Long matchId) {
        return FavoritesDto.from(playerService.addFavoriteMatch(playerId, matchId));
    }

    @Override
    @Transactional
    public FavoritesDto removeFavoriteMatch(Long playerId, Long matchId) {
        return FavoritesDto.from(playerService.removeFavoriteMatch(playerId, matchId));
    }
}
