package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    // сопственикот е најавениот партнер (од токенот)
    @PostMapping
    public ResponseEntity<VenueDto> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateVenueDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueApplicationService.create(userId(jwt), dto));
    }

    // само сопственикот може да го менува/брише својот терен
    @PutMapping("/{id}")
    public VenueDto update(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateVenueDto dto) {
        return venueApplicationService.update(id, userId(jwt), dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        venueApplicationService.deleteById(id, userId(jwt));
        return ResponseEntity.noContent().build();
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}