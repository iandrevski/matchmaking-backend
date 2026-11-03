package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.CreateMatchDto;
import sportify.matchmakingbackend.dto.MatchDto;
import sportify.matchmakingbackend.dto.NearbyMatchDto;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.service.application.MatchApplicationService;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchApplicationService matchApplicationService;

    public MatchController(MatchApplicationService matchApplicationService) {
        this.matchApplicationService = matchApplicationService;
    }

    // GET /api/matches?sportId=1&venueId=2&status=OPEN&date=2026-11-05
    @GetMapping
    public List<MatchDto> findAll(@RequestParam(required = false) Long sportId,
                                  @RequestParam(required = false) Long venueId,
                                  @RequestParam(required = false) MatchStatus status,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return matchApplicationService.findFiltered(sportId, venueId, status, date);
    }

    // GET /api/matches/days?from=2026-11-01&to=2026-11-30&venueId=2
    @GetMapping("/days")
    public List<LocalDate> findMatchDays(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                         @RequestParam(required = false) Long sportId,
                                         @RequestParam(required = false) Long venueId) {
        return matchApplicationService.findMatchDays(sportId, venueId, from, to);
    }

    // GET /api/matches/open?sportId=1 — натпревари на кои може да се приклучи
    @GetMapping("/open")
    public List<MatchDto> findOpenBySport(@RequestParam Long sportId) {
        return matchApplicationService.findOpenBySport(sportId);
    }

    @GetMapping("/venue/{venueId}")
    public List<MatchDto> findByVenue(@PathVariable Long venueId) {
        return matchApplicationService.findByVenue(venueId);
    }

    @GetMapping("/player/{playerId}")
    public List<MatchDto> findByPlayer(@PathVariable Long playerId) {
        return matchApplicationService.findByPlayer(playerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> findById(@PathVariable Long id) {
        return matchApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // организаторот е најавениот играч (од токенот), не query param
    @PostMapping
    public ResponseEntity<MatchDto> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateMatchDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchApplicationService.create(userId(jwt), dto));
    }

    @PostMapping("/{id}/join")
    public MatchDto join(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return matchApplicationService.join(id, userId(jwt));
    }

    @PostMapping("/{id}/leave")
    public MatchDto leave(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return matchApplicationService.leave(id, userId(jwt));
    }

    // само организаторот може да го откаже
    @PostMapping("/{id}/cancel")
    public MatchDto cancel(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return matchApplicationService.cancel(id, userId(jwt));
    }

    // "sub" од токенот = ID на најавениот корисник
    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    // GET /api/matches/nearby?lat=41.99&lng=21.43&radiusKm=10
    @GetMapping("/nearby")
    public List<NearbyMatchDto> findNearby(@RequestParam double lat,
                                           @RequestParam double lng,
                                           @RequestParam(defaultValue = "10") double radiusKm) {
        return matchApplicationService.findNearby(lat, lng, radiusKm);
    }
}