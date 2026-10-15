package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.VenueRepository;
import sportify.matchmakingbackend.service.domain.VenueService;

import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    @Override
    public List<Venue> findByOwner(Long ownerId) {
        return venueRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Venue> findByCity(String city) {
        return venueRepository.findByCityIgnoreCase(city);
    }

    @Override
    public List<Venue> findBySport(Long sportId) {
        return venueRepository.findBySportsId(sportId);
    }

    @Override
    @Transactional
    public Venue create(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    @Transactional
    public Venue update(Long id, Venue venue) {
        Venue existing = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", id));
        existing.setName(venue.getName());
        existing.setCity(venue.getCity());
        existing.setMunicipality(venue.getMunicipality());
        existing.setAddress(venue.getAddress());
        existing.setLatitude(venue.getLatitude());
        existing.setLongitude(venue.getLongitude());
        existing.setSports(venue.getSports());
        return venueRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venue", id);
        }
        venueRepository.deleteById(id);
    }
}
