package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.MessageDto;
import sportify.matchmakingbackend.service.application.ChatApplicationService;
import sportify.matchmakingbackend.service.domain.ChatService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChatApplicationServiceImpl implements ChatApplicationService {

    private final ChatService chatService;

    public ChatApplicationServiceImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public List<MessageDto> findMessages(Long matchId, Long playerId, Long afterId) {
        return MessageDto.from(chatService.findMessages(matchId, playerId, afterId));
    }

    @Override
    @Transactional
    public MessageDto send(Long matchId, Long playerId, String text) {
        return MessageDto.from(chatService.send(matchId, playerId, text));
    }
}