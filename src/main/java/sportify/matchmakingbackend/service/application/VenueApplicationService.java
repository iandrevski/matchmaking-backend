package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.CreateVenueDto;
import sportify.matchmakingbackend.dto.VenueDto;

import java.util.List;
import java.util.Optional;

public interface VenueApplicationService {

    List<VenueDto> findAll();

    Optional<VenueDto> findById(Long id);

    List<VenueDto> findByOwner(Long ownerId);

    List<VenueDto> findByCity(String city);

    List<VenueDto> findBySport(Long sportId);

    VenueDto create(Long ownerId, CreateVenueDto dto);

    VenueDto update(Long id, Long ownerId, CreateVenueDto dto);

    void deleteById(Long id, Long ownerId);
}
