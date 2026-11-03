package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.Venue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findByOwnerId(Long ownerId);

    List<Venue> findByCityIgnoreCase(String city);

    // објекти каде што може да се игра даден спорт
    List<Venue> findBySportsId(Long sportId);

    // терен што е нечиј омилен не може да се избрише додека постои таа врска
    @Modifying
    @Query(value = "delete from player_favorite_venues where venue_id = :venueId", nativeQuery = true)
    void deleteFavoriteLinks(@Param("venueId") Long venueId);
}
