package sportify.matchmakingbackend.service.domain.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Partner;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.Sport;
import sportify.matchmakingbackend.model.Venue;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Без база и без Spring: repository-ата се заменети со Mockito лажни објекти
@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    private static final Long MATCH_ID = 1L;
    private static final Long ORGANIZER_ID = 2L;
    private static final Long PLAYER_ID = 3L;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Sport football;
    private Venue venue;
    private Player organizer;
    private Player player;
    private Match match;

    @BeforeEach
    void setUp() {
        football = new Sport("Football", 3);
        football.setId(1L);

        venue = new Venue("Arena", "Skopje", "Centar", "Test 1", new Partner());
        venue.setId(1L);
        venue.getSports().add(football);

        organizer = player(ORGANIZER_ID, "marko");
        player = player(PLAYER_ID, "ana");

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        match = new Match(start, start.plusMinutes(90), football, venue, organizer);
        match.setId(MATCH_ID);
        match.getParticipants().add(organizer);

        // save го враќа истиот објект, како вистинскиот repository
        lenient().when(matchRepository.save(any(Match.class))).thenAnswer(call -> call.getArgument(0));
        lenient().when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        lenient().when(playerRepository.findById(PLAYER_ID)).thenReturn(Optional.of(player));
    }

    private static Player player(Long id, String username) {
        Player p = new Player(username, username + "@test.com", "hash");
        p.setId(id);
        return p;
    }

    private Match newMatch() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        return new Match(start, start.plusMinutes(90), football, venue, organizer);
    }

    // ---------- create ----------

    @Test
    void createAddsOrganizerAsParticipantAndStaysOpen() {
        Match created = matchService.create(newMatch());

        assertTrue(created.getParticipants().contains(organizer));
        assertEquals(MatchStatus.OPEN, created.getStatus());
    }

    @Test
    void createRejectsEndBeforeStart() {
        Match invalid = newMatch();
        invalid.setDateTo(invalid.getDateFrom().minusHours(1));

        assertThrows(InvalidOperationException.class, () -> matchService.create(invalid));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createRejectsSportNotOfferedByVenue() {
        Sport tennis = new Sport("Tennis", 2);
        tennis.setId(2L);
        Match invalid = newMatch();
        invalid.setSport(tennis);

        assertThrows(InvalidOperationException.class, () -> matchService.create(invalid));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createRejectsOverlappingBooking() {
        Match overlapping = newMatch();
        when(matchRepository.existsOverlappingMatch(venue.getId(), overlapping.getDateFrom(), overlapping.getDateTo()))
                .thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> matchService.create(overlapping));
        verify(matchRepository, never()).save(any());
    }

    // ---------- join ----------

    @Test
    void joinAddsPlayerAndKeepsMatchOpenWhileSpotsRemain() {
        Match joined = matchService.join(MATCH_ID, PLAYER_ID);

        assertTrue(joined.getParticipants().contains(player));
        assertEquals(MatchStatus.OPEN, joined.getStatus());
    }

    @Test
    void joinTakingLastSpotMarksMatchFull() {
        match.getParticipants().add(player(4L, "petar"));

        Match joined = matchService.join(MATCH_ID, PLAYER_ID);

        assertEquals(MatchStatus.FULL, joined.getStatus());
    }

    @Test
    void joinRejectsMatchThatIsNotOpen() {
        match.setStatus(MatchStatus.FULL);

        assertThrows(InvalidOperationException.class, () -> matchService.join(MATCH_ID, PLAYER_ID));
        assertFalse(match.getParticipants().contains(player));
    }

    @Test
    void joinRejectsMatchThatAlreadyStarted() {
        match.setDateFrom(LocalDateTime.now().minusMinutes(5));

        assertThrows(InvalidOperationException.class, () -> matchService.join(MATCH_ID, PLAYER_ID));
        assertFalse(match.getParticipants().contains(player));
    }

    @Test
    void joinRejectsPlayerWhoAlreadyJoined() {
        match.getParticipants().add(player);

        assertThrows(InvalidOperationException.class, () -> matchService.join(MATCH_ID, PLAYER_ID));
    }

    @Test
    void joinRejectsUnknownPlayer() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> matchService.join(MATCH_ID, 99L));
    }

    // ---------- leave ----------

    @Test
    void leaveRemovesPlayer() {
        match.getParticipants().add(player);

        Match left = matchService.leave(MATCH_ID, PLAYER_ID);

        assertFalse(left.getParticipants().contains(player));
    }

    @Test
    void leavingFullMatchReopensIt() {
        match.getParticipants().add(player);
        match.getParticipants().add(player(4L, "petar"));
        match.setStatus(MatchStatus.FULL);

        Match left = matchService.leave(MATCH_ID, PLAYER_ID);

        assertEquals(MatchStatus.OPEN, left.getStatus());
    }

    @Test
    void leaveRejectsPlayerWhoIsNotParticipant() {
        assertThrows(InvalidOperationException.class, () -> matchService.leave(MATCH_ID, PLAYER_ID));
    }

    @Test
    void leaveRejectsFinishedMatch() {
        match.getParticipants().add(player);
        match.setStatus(MatchStatus.FINISHED);

        assertThrows(InvalidOperationException.class, () -> matchService.leave(MATCH_ID, PLAYER_ID));
        assertTrue(match.getParticipants().contains(player));
    }

    // ---------- cancel ----------

    @Test
    void organizerCanCancelMatch() {
        Match cancelled = matchService.cancel(MATCH_ID, ORGANIZER_ID);

        assertEquals(MatchStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void cancelRejectsAnyoneElse() {
        assertThrows(ForbiddenOperationException.class, () -> matchService.cancel(MATCH_ID, PLAYER_ID));
        assertEquals(MatchStatus.OPEN, match.getStatus());
    }

    @Test
    void cancelRejectsFinishedMatch() {
        match.setStatus(MatchStatus.FINISHED);

        assertThrows(InvalidOperationException.class, () -> matchService.cancel(MATCH_ID, ORGANIZER_ID));
        assertEquals(MatchStatus.FINISHED, match.getStatus());
    }
}