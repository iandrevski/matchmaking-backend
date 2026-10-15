package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Sport;

import java.util.Collection;
import java.util.List;

public record SportDto(Long id, String name, Integer numberOfPlayers) {

    public static SportDto from(Sport sport) {
        return new SportDto(sport.getId(), sport.getName(), sport.getNumberOfPlayers());
    }

    public static List<SportDto> from(Collection<Sport> sports) {
        return sports.stream().map(SportDto::from).toList();
    }
}
