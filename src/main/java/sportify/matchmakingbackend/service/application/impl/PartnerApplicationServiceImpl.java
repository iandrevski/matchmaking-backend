package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.PartnerDto;
import sportify.matchmakingbackend.service.application.PartnerApplicationService;
import sportify.matchmakingbackend.service.domain.PartnerService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PartnerApplicationServiceImpl implements PartnerApplicationService {

    private final PartnerService partnerService;

    public PartnerApplicationServiceImpl(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Override
    public List<PartnerDto> findAll() {
        return PartnerDto.from(partnerService.findAll());
    }

    @Override
    public Optional<PartnerDto> findById(Long id) {
        return partnerService.findById(id).map(PartnerDto::from);
    }
}
