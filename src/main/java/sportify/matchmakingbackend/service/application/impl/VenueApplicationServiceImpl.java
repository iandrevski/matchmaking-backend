package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sportify.matchmakingbackend.dto.CreateVenueDto;
import sportify.matchmakingbackend.dto.VenueDto;
import sportify.matchmakingbackend.dto.VenueStatsDto;
import sportify.matchmakingbackend.model.Partner;
import sportify.matchmakingbackend.model.Sport;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.service.application.VenueApplicationService;
import sportify.matchmakingbackend.service.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VenueApplicationServiceImpl implements VenueApplicationService {

    private final PhotoStorageService photoStorageService;
    private final VenueService venueService;
    private final PartnerService partnerService;
    private final SportService sportService;
    private final MatchService matchService;

    public VenueApplicationServiceImpl(PhotoStorageService photoStorageService, VenueService venueService, PartnerService partnerService, SportService sportService, MatchService matchService) {
        this.photoStorageService = photoStorageService;
        this.venueService = venueService;
        this.partnerService = partnerService;
        this.sportService = sportService;
        this.matchService = matchService;
    }

    @Override
    public List<VenueDto> findAll() {
        return VenueDto.from(venueService.findAll());
    }

    @Override
    public Optional<VenueDto> findById(Long id) {
        return venueService.findById(id).map(VenueDto::from);
    }

    @Override
    public List<VenueDto> findByOwner(Long ownerId) {
        return VenueDto.from(venueService.findByOwner(ownerId));
    }

    @Override
    public List<VenueDto> findByCity(String city) {
        return VenueDto.from(venueService.findByCity(city));
    }

    @Override
    public List<VenueDto> findBySport(Long sportId) {
        return VenueDto.from(venueService.findBySport(sportId));
    }

    @Override
    @Transactional
    public VenueDto create(Long ownerId, CreateVenueDto dto) {
        Partner owner = partnerService.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner", ownerId));

        Venue venue = new Venue(dto.name(), dto.city(), dto.municipality(), dto.address(), owner);
        venue.setLatitude(dto.latitude());
        venue.setLongitude(dto.longitude());
        venue.setPricePerHour(dto.pricePerHour());
        venue.setSports(findSports(dto.sportIds()));

        return VenueDto.from(venueService.create(venue));
    }

    @Override
    @Transactional
    public VenueDto update(Long id, Long ownerId, CreateVenueDto dto) {
        Venue venue = new Venue();
        venue.setName(dto.name());
        venue.setCity(dto.city());
        venue.setMunicipality(dto.municipality());
        venue.setAddress(dto.address());
        venue.setLatitude(dto.latitude());
        venue.setLongitude(dto.longitude());
        venue.setPricePerHour(dto.pricePerHour());
        venue.setSports(findSports(dto.sportIds()));

        return VenueDto.from(venueService.update(id, ownerId, venue));    }

    @Override
    @Transactional
    public void deleteById(Long id, Long ownerId) {
        venueService.deleteById(id, ownerId);
    }

    @Override
    @Transactional
    public VenueDto addPhoto(Long id, Long ownerId, MultipartFile file) {
        String fileName = photoStorageService.store(file);
        try {
            return VenueDto.from(venueService.addPhoto(id, ownerId, fileName));
        } catch (RuntimeException e) {
            // теренот не е твој или има премногу слики → фајлот не смее да остане
            photoStorageService.delete(fileName);
            throw e;
        }
    }

    @Override
    @Transactional
    public VenueDto removePhoto(Long id, Long ownerId, String fileName) {
        VenueDto venue = VenueDto.from(venueService.removePhoto(id, ownerId, fileName));
        photoStorageService.delete(fileName);
        return venue;
    }

    private Set<Sport> findSports(List<Long> sportIds) {
        return sportIds.stream()
                .map(sportId -> sportService.findById(sportId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sport", sportId)))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public VenueStatsDto getStats(Long id, Long ownerId) {
        Venue venue = venueService.findOwned(id, ownerId);
        return VenueStatsDto.from(venue, matchService.findByVenue(id));
    }
}
