package sportify.matchmakingbackend.service.application;

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
}
