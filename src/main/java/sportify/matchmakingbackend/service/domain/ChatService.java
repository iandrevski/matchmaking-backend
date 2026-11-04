package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.MatchMessage;

import java.util.List;

public interface ChatService {

    // afterId == null → последните 50; инаку само поновите од afterId
    List<MatchMessage> findMessages(Long matchId, Long playerId, Long afterId);

    MatchMessage send(Long matchId, Long playerId, String text);
}