package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.Venue;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findByOwnerId(Long ownerId);

    List<Venue> findByCityIgnoreCase(String city);

    // објекти каде што може да се игра даден спорт
    List<Venue> findBySportsId(Long sportId);
}
