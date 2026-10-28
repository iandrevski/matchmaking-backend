package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.LocationDto;
import sportify.matchmakingbackend.dto.PlayerDto;
import sportify.matchmakingbackend.dto.UpdatePlayerDto;
import sportify.matchmakingbackend.service.application.PlayerApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerApplicationService playerApplicationService;

    public PlayerController(PlayerApplicationService playerApplicationService) {
        this.playerApplicationService = playerApplicationService;
    }

    @GetMapping
    public List<PlayerDto> findAll() {
        return playerApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> findById(@PathVariable Long id) {
        return playerApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // играчот може да го менува само СВОЈОТ профил → /me наместо /{id}
    @PutMapping("/me")
    public PlayerDto updateProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdatePlayerDto dto) {
        return playerApplicationService.updateProfile(userId(jwt), dto);
    }

    // моменталната локација ја праќа апликацијата (expo-location)
    @PutMapping("/me/location")
    public PlayerDto updateLocation(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody LocationDto dto) {
        return playerApplicationService.updateLocation(userId(jwt), dto);
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}