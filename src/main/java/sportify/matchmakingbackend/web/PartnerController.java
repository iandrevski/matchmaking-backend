package sportify.matchmakingbackend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.PartnerDto;
import sportify.matchmakingbackend.service.application.PartnerApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerApplicationService partnerApplicationService;

    public PartnerController(PartnerApplicationService partnerApplicationService) {
        this.partnerApplicationService = partnerApplicationService;
    }

    @GetMapping
    public List<PartnerDto> findAll() {
        return partnerApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerDto> findById(@PathVariable Long id) {
        return partnerApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
