package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.CreateSportDto;
import sportify.matchmakingbackend.dto.SportDto;
import sportify.matchmakingbackend.service.application.SportApplicationService;
import sportify.matchmakingbackend.service.domain.SportService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SportApplicationServiceImpl implements SportApplicationService {

    private final SportService sportService;

    public SportApplicationServiceImpl(SportService sportService) {
        this.sportService = sportService;
    }

    @Override
    public List<SportDto> findAll() {
        return SportDto.from(sportService.findAll());
    }

    @Override
    public Optional<SportDto> findById(Long id) {
        return sportService.findById(id).map(SportDto::from);
    }

    @Override
    @Transactional
    public SportDto create(CreateSportDto dto) {
        return SportDto.from(sportService.create(dto.toSport()));
    }

    @Override
    @Transactional
    public SportDto update(Long id, CreateSportDto dto) {
        return SportDto.from(sportService.update(id, dto.toSport()));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sportService.deleteById(id);
    }
}
