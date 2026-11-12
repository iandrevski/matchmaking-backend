package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {

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

    List<Match> findBySportId(Long sportId);

    // OPEN/FULL натпревари чие време завршило → FINISHED (еден UPDATE за сите)
    @Modifying
    @Query("""
        update Match m
        set m.status = sportify.matchmakingbackend.model.enums.MatchStatus.FINISHED
        where m.dateTo < :now
          and m.status in (sportify.matchmakingbackend.model.enums.MatchStatus.OPEN,
                           sportify.matchmakingbackend.model.enums.MatchStatus.FULL)
        """)
    int finishPastMatches(@Param("now") LocalDateTime now);

    // истиот услов како finishPastMatches, за да знаеме КОИ натпревари се затвораат
    @Query("""
        select m.id from Match m
        where m.dateTo < :now
          and m.status in (sportify.matchmakingbackend.model.enums.MatchStatus.OPEN,
                           sportify.matchmakingbackend.model.enums.MatchStatus.FULL)
        """)
    List<Long> findIdsToFinish(@Param("now") LocalDateTime now);

    // за потсетникот „почнува наскоро“
    List<Match> findByDateFromBetweenAndStatusIn(LocalDateTime from, LocalDateTime to, Collection<MatchStatus> statuses);

    // дали двајца играчи биле во ист натпревар
    @Query("""
            select count(m) > 0 from Match m
            join m.participants a
            join m.participants b
            where a.id = :firstId and b.id = :secondId
            """)
    boolean playedTogether(@Param("firstId") Long firstId, @Param("secondId") Long secondId);
}
