package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Venue;

import java.util.List;

public record VenueDto(
        Long id,
        String name,
        String city,
        String municipality,
        String address,
        Double latitude,
        Double longitude,
        Long ownerId,
        List<SportDto> sports
) {

    public static VenueDto from(Venue venue) {
        return new VenueDto(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getMunicipality(),
                venue.getAddress(),
                venue.getLatitude(),
                venue.getLongitude(),
                venue.getOwner().getId(),
                SportDto.from(venue.getSports())
        );
    }

    public static List<VenueDto> from(List<Venue> venues) {
        return venues.stream().map(VenueDto::from).toList();
    }
}
