package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.CreateMatchDto;
import sportify.matchmakingbackend.dto.MatchDto;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.Sport;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.service.application.MatchApplicationService;
import sportify.matchmakingbackend.service.domain.MatchService;
import sportify.matchmakingbackend.service.domain.PlayerService;
import sportify.matchmakingbackend.service.domain.SportService;
import sportify.matchmakingbackend.service.domain.VenueService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MatchApplicationServiceImpl implements MatchApplicationService {

    private final MatchService matchService;
    private final SportService sportService;
    private final VenueService venueService;
    private final PlayerService playerService;

    public MatchApplicationServiceImpl(MatchService matchService, SportService sportService,
                                       VenueService venueService, PlayerService playerService) {
        this.matchService = matchService;
        this.sportService = sportService;
        this.venueService = venueService;
        this.playerService = playerService;
    }

    @Override
    public List<MatchDto> findAll() {
        return MatchDto.from(matchService.findAll());
    }

    @Override
    public Optional<MatchDto> findById(Long id) {
        return matchService.findById(id).map(MatchDto::from);
    }

    @Override
    public List<MatchDto> findByStatus(MatchStatus status) {
        return MatchDto.from(matchService.findByStatus(status));
    }

    @Override
    public List<MatchDto> findOpenBySport(Long sportId) {
        return MatchDto.from(matchService.findOpenBySport(sportId));
    }

    @Override
    public List<MatchDto> findByVenue(Long venueId) {
        return MatchDto.from(matchService.findByVenue(venueId));
    }

    @Override
    public List<MatchDto> findByPlayer(Long playerId) {
        return MatchDto.from(matchService.findByPlayer(playerId));
    }

    @Override
    @Transactional
    public MatchDto create(Long organizerId, CreateMatchDto dto) {
        Sport sport = sportService.findById(dto.sportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", dto.sportId()));
        Venue venue = venueService.findById(dto.venueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue", dto.venueId()));
        Player organizer = playerService.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", organizerId));

        Match match = new Match(dto.dateFrom(), dto.dateTo(), sport, venue, organizer);
        return MatchDto.from(matchService.create(match));
    }

    @Override
    @Transactional
    public MatchDto join(Long matchId, Long playerId) {
        return MatchDto.from(matchService.join(matchId, playerId));
    }

    @Override
    @Transactional
    public MatchDto leave(Long matchId, Long playerId) {
        return MatchDto.from(matchService.leave(matchId, playerId));
    }

    @Override
    @Transactional
    public MatchDto cancel(Long matchId) {
        return MatchDto.from(matchService.cancel(matchId));
    }
}
