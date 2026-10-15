package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.enums.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findBySportIdAndStatus(Long sportId, MatchStatus status);

    List<Match> findByVenueId(Long venueId);

    List<Match> findByOrganizerId(Long organizerId);

    // натпревари во кои играчот учествува
    List<Match> findByParticipantsId(Long playerId);

    // дали објектот е зафатен во даден термин (преклопување на интервали)
    @Query("""
            select count(m) > 0 from Match m
            where m.venue.id = :venueId
              and m.status <> sportify.matchmakingbackend.model.enums.MatchStatus.CANCELLED
              and m.dateFrom < :dateTo
              and m.dateTo > :dateFrom
            """)
    boolean existsOverlappingMatch(@Param("venueId") Long venueId,
                                   @Param("dateFrom") LocalDateTime dateFrom,
                                   @Param("dateTo") LocalDateTime dateTo);
}
