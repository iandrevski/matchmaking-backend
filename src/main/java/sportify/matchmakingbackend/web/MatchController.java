package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.CreateMatchDto;
import sportify.matchmakingbackend.dto.MatchDto;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.service.application.MatchApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchApplicationService matchApplicationService;

    public MatchController(MatchApplicationService matchApplicationService) {
        this.matchApplicationService = matchApplicationService;
    }

    // GET /api/matches | ?status=OPEN
    @GetMapping
    public List<MatchDto> findAll(@RequestParam(required = false) MatchStatus status) {
        return status != null
                ? matchApplicationService.findByStatus(status)
                : matchApplicationService.findAll();
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

    // TODO: organizerId / playerId да се земат од логираниот играч (JWT) наместо од query param
    @PostMapping
    public ResponseEntity<MatchDto> create(@RequestParam Long organizerId, @Valid @RequestBody CreateMatchDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchApplicationService.create(organizerId, dto));
    }

    @PostMapping("/{id}/join")
    public MatchDto join(@PathVariable Long id, @RequestParam Long playerId) {
        return matchApplicationService.join(id, playerId);
    }

    @PostMapping("/{id}/leave")
    public MatchDto leave(@PathVariable Long id, @RequestParam Long playerId) {
        return matchApplicationService.leave(id, playerId);
    }

    @PostMapping("/{id}/cancel")
    public MatchDto cancel(@PathVariable Long id) {
        return matchApplicationService.cancel(id);
    }
}
