package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.events.MatchEvents;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.MatchMessage;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchMessageRepository;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.service.domain.ChatService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final MatchRepository matchRepository;
    private final MatchMessageRepository messageRepository;
    private final ApplicationEventPublisher events;

    public ChatServiceImpl(MatchRepository matchRepository, MatchMessageRepository messageRepository, ApplicationEventPublisher events) {
        this.matchRepository = matchRepository;
        this.messageRepository = messageRepository;
        this.events = events;
    }

    @Override
    public List<MatchMessage> findMessages(Long matchId, Long playerId, Long afterId) {
        participant(getMatch(matchId), playerId);
        if (afterId != null) {
            return messageRepository.findByMatchIdAndIdGreaterThanOrderByIdAsc(matchId, afterId);
        }
        // базата ги враќа најновите први, апликацијата ги сака по ред
        List<MatchMessage> latest = new ArrayList<>(messageRepository.findTop50ByMatchIdOrderByIdDesc(matchId));
        Collections.reverse(latest);
        return latest;
    }

    @Override
    @Transactional
    public MatchMessage send(Long matchId, Long playerId, String text) {
        Match match = getMatch(matchId);
        Player sender = participant(match, playerId);
        if (match.getStatus() == MatchStatus.CANCELLED) {
            throw new InvalidOperationException("This match was cancelled, the chat is closed");
        }
        MatchMessage message = messageRepository.save(new MatchMessage(match, sender, text.trim()));
        events.publishEvent(new MatchEvents.MessageSent(matchId, playerId, message.getText()));
        return message;    }

    private Match getMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));
    }

    // само учесниците го читаат и пишуваат чатот
    private static Player participant(Match match, Long playerId) {
        return match.getParticipants().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new ForbiddenOperationException("Only players in this match can use its chat"));
    }
}