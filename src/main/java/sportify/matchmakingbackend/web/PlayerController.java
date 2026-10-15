package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}")
    public PlayerDto updateProfile(@PathVariable Long id, @Valid @RequestBody UpdatePlayerDto dto) {
        return playerApplicationService.updateProfile(id, dto);
    }

    // моменталната локација ја праќа апликацијата (expo-location)
    @PutMapping("/{id}/location")
    public PlayerDto updateLocation(@PathVariable Long id, @Valid @RequestBody LocationDto dto) {
        return playerApplicationService.updateLocation(id, dto);
    }
}
