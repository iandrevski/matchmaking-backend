package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Player;

import java.util.List;

public record PlayerDto(
        Long id,
        String username,
        String email,
        Integer age,
        Double height,
        Double weight,
        Double homeLat,
        Double homeLong
) {

    public static PlayerDto from(Player player) {
        return new PlayerDto(
                player.getId(),
                player.getUsername(),
                player.getEmail(),
                player.getAge(),
                player.getHeight(),
                player.getWeight(),
                player.getHomeLat(),
                player.getHomeLong()
        );
    }

    public static List<PlayerDto> from(List<Player> players) {
        return players.stream().map(PlayerDto::from).toList();
    }
}
