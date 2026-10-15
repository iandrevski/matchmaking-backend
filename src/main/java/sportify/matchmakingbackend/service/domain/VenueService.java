package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueService {

    List<Venue> findAll();

    Optional<Venue> findById(Long id);

    List<Venue> findByOwner(Long ownerId);

    List<Venue> findByCity(String city);

    List<Venue> findBySport(Long sportId);

    Venue create(Venue venue);

    Venue update(Long id, Venue venue);

    void deleteById(Long id);
}
