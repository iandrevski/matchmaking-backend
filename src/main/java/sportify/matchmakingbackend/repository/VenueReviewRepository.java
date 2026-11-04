package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.VenueReview;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueReviewRepository extends JpaRepository<VenueReview, Long> {

    Optional<VenueReview> findByMatchIdAndAuthorId(Long matchId, Long authorId);

    List<VenueReview> findByVenueIdOrderByUpdatedAtDesc(Long venueId);
}