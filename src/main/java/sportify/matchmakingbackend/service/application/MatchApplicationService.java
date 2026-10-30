package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.CreateMatchDto;
import sportify.matchmakingbackend.dto.MatchDto;
import sportify.matchmakingbackend.dto.NearbyMatchDto;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.util.List;
import java.util.Optional;

public interface MatchApplicationService {

    List<MatchDto> findAll();

    Optional<MatchDto> findById(Long id);

    List<MatchDto> findByStatus(MatchStatus status);

    List<MatchDto> findOpenBySport(Long sportId);

    List<MatchDto> findByVenue(Long venueId);

    List<MatchDto> findByPlayer(Long playerId);

    MatchDto create(Long organizerId, CreateMatchDto dto);

    MatchDto join(Long matchId, Long playerId);

    MatchDto leave(Long matchId, Long playerId);

    MatchDto cancel(Long matchId, Long requesterId);

    List<MatchDto> findFiltered(Long sportId, MatchStatus status);

    List<NearbyMatchDto> findNearby(double lat, double lng, double radiusKm);
}
