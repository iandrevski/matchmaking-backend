package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.MatchMessage;

import java.time.LocalDateTime;
import java.util.List;

public record MessageDto(Long id, Long senderId, String senderUsername, String text, LocalDateTime createdAt) {

    public static MessageDto from(MatchMessage message) {
        return new MessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getText(),
                message.getCreatedAt()
        );
    }

    public static List<MessageDto> from(List<MatchMessage> messages) {
        return messages.stream().map(MessageDto::from).toList();
    }
}