package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Sport;

import java.util.List;
import java.util.Optional;

public interface SportService {

    List<Sport> findAll();

    Optional<Sport> findById(Long id);

    Sport create(Sport sport);

    Sport update(Long id, Sport sport);

    void deleteById(Long id);
}
