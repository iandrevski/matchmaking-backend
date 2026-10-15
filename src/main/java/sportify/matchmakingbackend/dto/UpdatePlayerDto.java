package sportify.matchmakingbackend.dto;

import jakarta.validation.constraints.Positive;
import sportify.matchmakingbackend.model.Player;

public record UpdatePlayerDto(
        @Positive Integer age,
        @Positive Double height,
        @Positive Double weight,
        Double homeLat,
        Double homeLong
) {

    public Player toPlayer() {
        Player player = new Player();
        player.setAge(age);
        player.setHeight(height);
        player.setWeight(weight);
        player.setHomeLat(homeLat);
        player.setHomeLong(homeLong);
        return player;
    }
}
