package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Player;

import java.util.Comparator;
import java.util.List;

// омилените терени и натпревари на играчот
public record FavoritesDto(List<VenueDto> venues, List<MatchDto> matches) {

    public static FavoritesDto from(Player player) {
        return new FavoritesDto(
                player.getFavoriteVenues().stream()
                        .sorted(Comparator.comparing(v -> v.getName().toLowerCase()))
                        .map(VenueDto::from)
                        .toList(),
                player.getFavoriteMatches().stream()
                        .sorted(Comparator.comparing(m -> m.getDateFrom()))
                        .map(MatchDto::from)
                        .toList()
        );
    }
}