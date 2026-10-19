package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MatchDto(
        Long id,
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        MatchStatus status,
        SportDto sport,
        Long venueId,
        String venueName,
        Long organizerId,
        String organizerUsername,
        int participantsCount,
        List<Long> participantIds
) {

    public static MatchDto from(Match match) {
        return new MatchDto(
                match.getId(),
                match.getDateFrom(),
                match.getDateTo(),
                match.getStatus(),
                SportDto.from(match.getSport()),
                match.getVenue().getId(),
                match.getVenue().getName(),
                match.getOrganizer() != null ? match.getOrganizer().getId() : null,
                match.getOrganizer() != null ? match.getOrganizer().getUsername() : null,
                match.getParticipants().size(),
                match.getParticipants().stream().map(participant -> participant.getId()).toList()
        );}

    public static List<MatchDto> from(List<Match> matches) {
        return matches.stream().map(MatchDto::from).toList();
    }
}
