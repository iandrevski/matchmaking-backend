package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.CreateSportDto;
import sportify.matchmakingbackend.dto.SportDto;
import sportify.matchmakingbackend.service.application.SportApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportApplicationService sportApplicationService;

    public SportController(SportApplicationService sportApplicationService) {
        this.sportApplicationService = sportApplicationService;
    }

    @GetMapping
    public List<SportDto> findAll() {
        return sportApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportDto> findById(@PathVariable Long id) {
        return sportApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SportDto> create(@Valid @RequestBody CreateSportDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sportApplicationService.create(dto));
    }

    @PutMapping("/{id}")
    public SportDto update(@PathVariable Long id, @Valid @RequestBody CreateSportDto dto) {
        return sportApplicationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sportApplicationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
