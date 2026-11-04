package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.MyMatchReviewsDto;
import sportify.matchmakingbackend.dto.PlayerReviewSummaryDto;
import sportify.matchmakingbackend.dto.ReviewInputDto;
import sportify.matchmakingbackend.dto.VenueReviewsDto;
import sportify.matchmakingbackend.model.Match;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.PlayerReview;
import sportify.matchmakingbackend.model.VenueReview;
import sportify.matchmakingbackend.model.enums.MatchStatus;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.service.application.ReviewApplicationService;
import sportify.matchmakingbackend.service.domain.MatchService;
import sportify.matchmakingbackend.service.domain.PlayerService;
import sportify.matchmakingbackend.service.domain.ReviewService;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReviewApplicationServiceImpl implements ReviewApplicationService {

    // просекот се покажува дури од толку оценки, дотогаш „New player“
    private static final int MIN_REVIEWS_FOR_AVERAGE = 3;

    private final ReviewService reviewService;
    private final MatchService matchService;
    private final PlayerService playerService;

    public ReviewApplicationServiceImpl(ReviewService reviewService, MatchService matchService,
                                        PlayerService playerService) {
        this.reviewService = reviewService;
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @Override
    public MyMatchReviewsDto getMine(Long matchId, Long playerId) {
        Match match = matchService.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId));
        return new MyMatchReviewsDto(
                reviewService.canReview(match, playerId),
                reviewService.reviewableUntil(match),
                reviewService.findGivenPlayerReviews(matchId, playerId).stream()
                        .map(MyMatchReviewsDto.GivenPlayerReview::from)
                        .toList(),
                reviewService.findGivenVenueReview(matchId, playerId)
                        .map(MyMatchReviewsDto.GivenVenueReview::from)
                        .orElse(null)
        );
    }

    @Override
    @Transactional
    public MyMatchReviewsDto submit(Long matchId, Long playerId, ReviewInputDto input) {
        reviewService.submit(matchId, playerId, input);
        return getMine(matchId, playerId);
    }

    @Override
    public PlayerReviewSummaryDto getPlayerSummary(Long playerId) {
        Player player = playerService.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", playerId));
        List<PlayerReview> received = reviewService.findReceived(playerId);

        Double average = received.size() < MIN_REVIEWS_FOR_AVERAGE ? null
                : Math.round(received.stream().mapToInt(PlayerReview::getStars).average().orElse(0) * 10) / 10.0;

        List<PlayerReviewSummaryDto.TagCount> tags = received.stream()
                .flatMap(r -> r.getTags().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new PlayerReviewSummaryDto.TagCount(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(PlayerReviewSummaryDto.TagCount::count).reversed())
                .toList();

        int matchesPlayed = (int) matchService.findByPlayer(playerId).stream()
                .filter(m -> m.getStatus() == MatchStatus.FINISHED)
                .count();

        return new PlayerReviewSummaryDto(player.getId(), player.getUsername(), received.size(), average, tags, matchesPlayed);
    }

    @Override
    public VenueReviewsDto getVenueReviews(Long venueId) {
        List<VenueReview> reviews = reviewService.findVenueReviews(venueId);
        Double average = reviews.isEmpty() ? null
                : Math.round(reviews.stream().mapToInt(VenueReview::getStars).average().orElse(0) * 10) / 10.0;
        return new VenueReviewsDto(average, reviews.size(), reviews.stream().map(VenueReviewsDto.Item::from).toList());
    }
}