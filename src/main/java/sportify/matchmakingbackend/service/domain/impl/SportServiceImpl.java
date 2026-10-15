package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Sport;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.SportRepository;
import sportify.matchmakingbackend.service.domain.SportService;

import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    public SportServiceImpl(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Override
    public List<Sport> findAll() {
        return sportRepository.findAll();
    }

    @Override
    public Optional<Sport> findById(Long id) {
        return sportRepository.findById(id);
    }

    @Override
    @Transactional
    public Sport create(Sport sport) {
        if (sportRepository.existsByNameIgnoreCase(sport.getName())) {
            throw new InvalidOperationException("Sport '" + sport.getName() + "' already exists");
        }
        return sportRepository.save(sport);
    }

    @Override
    @Transactional
    public Sport update(Long id, Sport sport) {
        Sport existing = sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport", id));
        existing.setName(sport.getName());
        existing.setNumberOfPlayers(sport.getNumberOfPlayers());
        return sportRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!sportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sport", id);
        }
        sportRepository.deleteById(id);
    }
}
