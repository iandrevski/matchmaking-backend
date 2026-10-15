package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.LocationDto;
import sportify.matchmakingbackend.dto.PlayerDto;
import sportify.matchmakingbackend.dto.UpdatePlayerDto;
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
}
