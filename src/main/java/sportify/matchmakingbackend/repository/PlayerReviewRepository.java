package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.PlayerReview;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerReviewRepository extends JpaRepository<PlayerReview, Long> {

    List<PlayerReview> findByMatchIdAndReviewerId(Long matchId, Long reviewerId);

    Optional<PlayerReview> findByMatchIdAndReviewerIdAndReviewedId(Long matchId, Long reviewerId, Long reviewedId);

    List<PlayerReview> findByReviewedId(Long reviewedId);
}