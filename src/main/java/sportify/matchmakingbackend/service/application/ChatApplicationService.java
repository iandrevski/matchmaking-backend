package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.MessageDto;

import java.util.List;

public interface ChatApplicationService {

    List<MessageDto> findMessages(Long matchId, Long playerId, Long afterId);

    MessageDto send(Long matchId, Long playerId, String text);
}