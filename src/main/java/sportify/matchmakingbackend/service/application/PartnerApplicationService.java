package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.PartnerDto;

import java.util.List;
import java.util.Optional;

public interface PartnerApplicationService {

    List<PartnerDto> findAll();

    Optional<PartnerDto> findById(Long id);
}
