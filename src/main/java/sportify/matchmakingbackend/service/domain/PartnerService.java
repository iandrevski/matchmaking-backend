package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.Partner;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    List<Partner> findAll();

    Optional<Partner> findById(Long id);

    Optional<Partner> findByUsername(String username);
}
