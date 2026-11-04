package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.ReviewInputDto;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.PlayerReview;
import sportify.matchmakingbackend.model.VenueReview;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ForbiddenOperationException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerReviewRepository;
import sportify.matchmakingbackend.repository.VenueReviewRepository;
import sportify.matchmakingbackend.service.domain.ReviewService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final int REVIEW_WINDOW_DAYS = 7;

    private final MatchRepository matchRepository;
    private final PlayerReviewRepository playerReviewRepository;
    private final VenueReviewRepository venueReviewRepository;

    public ReviewServiceImpl(MatchRepository matchRepository,
                             PlayerReviewRepository playerReviewRepository,
                             VenueReviewRepository venueReviewRepository) {
        this.matchRepository = matchRepository;
        this.playerReviewRepository = playerReviewRepository;
        this.venueReviewRepository = venueReviewRepository;
    }

    @Override
    public LocalDateTime reviewableUntil(Match match) {
        return match.getDateTo().plusDays(REVIEW_WINDOW_DAYS);
    }

    @Override
    public boolean canReview(Match match, Long playerId) {
        return match.getStatus() == MatchStatus.FINISHED
                && LocalDateTime.now().isBefore(reviewableUntil(match))
                && findParticipant(match, playerId).isPresent();
    }

    @Override
    @Transactional
    public void submit(Long matchId, Long reviewerId, ReviewInputDto input) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));

        Player reviewer = findParticipant(match, reviewerId)
                .orElseThrow(() -> new ForbiddenOperationException("Only participants can review this match"));
        if (match.getStatus() != MatchStatus.FINISHED) {
            throw new InvalidOperationException("You can review a match only after it is finished");
        }
        if (!LocalDateTime.now().isBefore(reviewableUntil(match))) {
            throw new InvalidOperationException("The review period for this match has ended");
        }

        LocalDateTime now = LocalDateTime.now();

        for (ReviewInputDto.PlayerInput item : input.players()) {
            if (item.playerId().equals(reviewerId)) {
                throw new InvalidOperationException("You cannot review yourself");
            }
            Player reviewed = findParticipant(match, item.playerId())
                    .orElseThrow(() -> new InvalidOperationException("Player " + item.playerId() + " did not play in this match"));

            // постоечката оценка се менува, нова не се дуплира
            PlayerReview review = playerReviewRepository
                    .findByMatchIdAndReviewerIdAndReviewedId(matchId, reviewerId, reviewed.getId())
                    .orElseGet(() -> new PlayerReview(match, reviewer, reviewed));
            review.setStars(item.stars());
            review.getTags().clear();
            if (item.tags() != null) {
                review.getTags().addAll(item.tags());
            }
            review.setUpdatedAt(now);
            playerReviewRepository.save(review);
        }

        if (input.venue() != null) {
            VenueReview review = venueReviewRepository.findByMatchIdAndAuthorId(matchId, reviewerId)
                    .orElseGet(() -> new VenueReview(match, match.getVenue(), reviewer));
            review.setStars(input.venue().stars());
            String comment = input.venue().comment();
            review.setComment(comment == null || comment.isBlank() ? null : comment.trim());
            review.setUpdatedAt(now);
            venueReviewRepository.save(review);
        }
    }

    @Override
    public List<PlayerReview> findGivenPlayerReviews(Long matchId, Long reviewerId) {
        return playerReviewRepository.findByMatchIdAndReviewerId(matchId, reviewerId);
    }

    @Override
    public Optional<VenueReview> findGivenVenueReview(Long matchId, Long reviewerId) {
        return venueReviewRepository.findByMatchIdAndAuthorId(matchId, reviewerId);
    }

    @Override
    public List<PlayerReview> findReceived(Long playerId) {
        return playerReviewRepository.findByReviewedId(playerId);
    }

    @Override
    public List<VenueReview> findVenueReviews(Long venueId) {
        return venueReviewRepository.findByVenueIdOrderByUpdatedAtDesc(venueId);
    }

    private static Optional<Player> findParticipant(Match match, Long playerId) {
        return match.getParticipants().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst();
    }
}