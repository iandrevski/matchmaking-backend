package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Partner;

import java.util.List;

public record PartnerDto(Long id, String username, String email) {

    public static PartnerDto from(Partner partner) {
        return new PartnerDto(partner.getId(), partner.getUsername(), partner.getEmail());
    }

    public static List<PartnerDto> from(List<Partner> partners) {
        return partners.stream().map(PartnerDto::from).toList();
    }
}
