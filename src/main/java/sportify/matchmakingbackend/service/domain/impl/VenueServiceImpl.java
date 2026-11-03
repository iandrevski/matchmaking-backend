package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.VenueRepository;
import sportify.matchmakingbackend.service.domain.VenueService;

import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    private static final int MAX_PHOTOS = 8;

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
    public Venue update(Long id, Long ownerId, Venue venue) {
        Venue existing = getOwnedVenue(id, ownerId);
        existing.setName(venue.getName());
        existing.setCity(venue.getCity());
        existing.setMunicipality(venue.getMunicipality());
        existing.setAddress(venue.getAddress());
        existing.setLatitude(venue.getLatitude());
        existing.setLongitude(venue.getLongitude());
        existing.setPricePerHour(venue.getPricePerHour());
        existing.setSports(venue.getSports());
        return venueRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long id, Long ownerId) {
        Venue venue = getOwnedVenue(id, ownerId);
        venueRepository.deleteFavoriteLinks(id);
        venueRepository.delete(venue);
    }

    @Override
    @Transactional
    public Venue addPhoto(Long id, Long ownerId, String fileName) {
        Venue venue = getOwnedVenue(id, ownerId);
        if (venue.getPhotos().size() >= MAX_PHOTOS) {
            throw new InvalidOperationException("A venue can have at most " + MAX_PHOTOS + " photos");
        }
        venue.getPhotos().add(fileName);
        return venueRepository.save(venue);
    }

    @Override
    @Transactional
    public Venue removePhoto(Long id, Long ownerId, String fileName) {
        Venue venue = getOwnedVenue(id, ownerId);
        if (!venue.getPhotos().remove(fileName)) {
            throw new InvalidOperationException("Photo not found on this venue");
        }
        return venueRepository.save(venue);
    }

    // теренот мора да постои И да му припаѓа на тој партнер
    private Venue getOwnedVenue(Long id, Long ownerId) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", id));
        if (!venue.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenOperationException("You can only manage your own venues");
        }
        return venue;
    }

    @Override
    public Venue findOwned(Long id, Long ownerId) {
        return getOwnedVenue(id, ownerId);
    }

}
