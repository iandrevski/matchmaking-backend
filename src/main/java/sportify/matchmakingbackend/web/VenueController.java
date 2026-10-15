package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.CreateVenueDto;
import sportify.matchmakingbackend.dto.VenueDto;
import sportify.matchmakingbackend.service.application.VenueApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueApplicationService venueApplicationService;

    public VenueController(VenueApplicationService venueApplicationService) {
        this.venueApplicationService = venueApplicationService;
    }

    // GET /api/venues?city=Skopje | ?sportId=1 | ?ownerId=2
    @GetMapping
    public List<VenueDto> findAll(@RequestParam(required = false) String city,
                                  @RequestParam(required = false) Long sportId,
                                  @RequestParam(required = false) Long ownerId) {
        if (ownerId != null) {
            return venueApplicationService.findByOwner(ownerId);
        }
        if (sportId != null) {
            return venueApplicationService.findBySport(sportId);
        }
        if (city != null && !city.isBlank()) {
            return venueApplicationService.findByCity(city);
        }
        return venueApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> findById(@PathVariable Long id) {
        return venueApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // TODO: ownerId да се земе од логираниот партнер (JWT) наместо од query param
    @PostMapping
    public ResponseEntity<VenueDto> create(@RequestParam Long ownerId, @Valid @RequestBody CreateVenueDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueApplicationService.create(ownerId, dto));
    }

    @PutMapping("/{id}")
    public VenueDto update(@PathVariable Long id, @Valid @RequestBody CreateVenueDto dto) {
        return venueApplicationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueApplicationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
