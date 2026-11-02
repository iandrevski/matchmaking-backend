package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Player;

// само јавни податоци за играчот во натпреварот (без email)
public record ParticipantDto(Long id, String username) {

    public static ParticipantDto from(Player player) {
        return new ParticipantDto(player.getId(), player.getUsername());
    }
}