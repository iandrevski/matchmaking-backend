package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.service.domain.MatchService;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    public MatchServiceImpl(MatchRepository matchRepository, PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }

    @Override
    public List<Match> findByStatus(MatchStatus status) {
        return matchRepository.findByStatus(status);
    }

    @Override
    public List<Match> findOpenBySport(Long sportId) {
        return matchRepository.findBySportIdAndStatus(sportId, MatchStatus.OPEN);
    }

    @Override
    public List<Match> findByVenue(Long venueId) {
        return matchRepository.findByVenueId(venueId);
    }

    @Override
    public List<Match> findByPlayer(Long playerId) {
        return matchRepository.findByParticipantsId(playerId);
    }

    @Override
    @Transactional
    public Match create(Match match) {
        if (!match.getDateFrom().isBefore(match.getDateTo())) {
            throw new InvalidOperationException("Match start must be before its end");
        }
        boolean venueSupportsSport = match.getVenue().getSports().stream()
                .anyMatch(s -> s.getId().equals(match.getSport().getId()));
        if (!venueSupportsSport) {
            throw new InvalidOperationException("Venue does not support this sport");
        }
        if (matchRepository.existsOverlappingMatch(match.getVenue().getId(), match.getDateFrom(), match.getDateTo())) {
            throw new InvalidOperationException("Venue is already booked in this time slot");
        }

        // организаторот автоматски е и учесник
        if (match.getOrganizer() != null) {
            match.getParticipants().add(match.getOrganizer());
        }
        match.setStatus(match.isFull() ? MatchStatus.FULL : MatchStatus.OPEN);
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match join(Long matchId, Long playerId) {
        Match match = getMatch(matchId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", playerId));

        if (match.getStatus() != MatchStatus.OPEN) {
            throw new InvalidOperationException("Match is not open for joining");
        }
        if (!match.getDateFrom().isAfter(LocalDateTime.now())) {
            throw new InvalidOperationException("Match has already started");
        }
        boolean alreadyJoined = match.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(playerId));
        if (alreadyJoined) {
            throw new InvalidOperationException("Player already joined this match");
        }

        match.getParticipants().add(player);
        if (match.isFull()) {
            match.setStatus(MatchStatus.FULL);
        }
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match leave(Long matchId, Long playerId) {
        Match match = getMatch(matchId);

        if (match.getStatus() == MatchStatus.FINISHED || match.getStatus() == MatchStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot leave a finished or cancelled match");
        }
        boolean removed = match.getParticipants().removeIf(p -> p.getId().equals(playerId));
        if (!removed) {
            throw new InvalidOperationException("Player is not a participant of this match");
        }

        // се ослободи место
        if (match.getStatus() == MatchStatus.FULL) {
            match.setStatus(MatchStatus.OPEN);
        }
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match cancel(Long matchId, Long requesterId) {
        Match match = getMatch(matchId);
        if (match.getOrganizer() == null || !match.getOrganizer().getId().equals(requesterId)) {
            throw new ForbiddenOperationException("Only the organizer can cancel this match");
        }
        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new InvalidOperationException("Cannot cancel a finished match");
        }
        match.setStatus(MatchStatus.CANCELLED);
        return matchRepository.save(match);
    }

    @Override
    public List<Match> findFiltered(Long sportId, MatchStatus status) {
        if (sportId != null && status != null) {
            return matchRepository.findBySportIdAndStatus(sportId, status);
        } else if (sportId != null) {
            return matchRepository.findBySportId(sportId);
        } else if (status != null) {
            return matchRepository.findByStatus(status);
        } else {
            return matchRepository.findAll();
        }
    }

    private Match getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    @Override
    @Transactional
    public int finishPastMatches() {
        return matchRepository.finishPastMatches(LocalDateTime.now());
    }
}
