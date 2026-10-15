package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import sportify.matchmakingbackend.model.Partner;
import sportify.matchmakingbackend.repository.PartnerRepository;
import sportify.matchmakingbackend.service.domain.PartnerService;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public List<Partner> findAll() {
        return partnerRepository.findAll();
    }

    @Override
    public Optional<Partner> findById(Long id) {
        return partnerRepository.findById(id);
    }

    @Override
    public Optional<Partner> findByUsername(String username) {
        return partnerRepository.findByUsername(username);
    }
}
