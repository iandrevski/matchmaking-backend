package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.CreateSportDto;
import sportify.matchmakingbackend.dto.SportDto;

import java.util.List;
import java.util.Optional;

public interface SportApplicationService {

    List<SportDto> findAll();

    Optional<SportDto> findById(Long id);

    SportDto create(CreateSportDto dto);

    SportDto update(Long id, CreateSportDto dto);

    void deleteById(Long id);
}
