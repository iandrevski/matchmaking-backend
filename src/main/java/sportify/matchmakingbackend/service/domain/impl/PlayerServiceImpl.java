package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.MatchRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.repository.VenueRepository;
import sportify.matchmakingbackend.service.domain.PlayerService;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final VenueRepository venueRepository;
    private final MatchRepository matchRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, VenueRepository venueRepository, MatchRepository matchRepository) {
        this.playerRepository = playerRepository;
        this.venueRepository = venueRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Player updateProfile(Long id, Player player) {
        Player existing = getPlayer(id);
        existing.setAge(player.getAge());
        existing.setHeight(player.getHeight());
        existing.setWeight(player.getWeight());
        existing.setHomeLat(player.getHomeLat());
        existing.setHomeLong(player.getHomeLong());
        return playerRepository.save(existing);
    }

    @Override
    @Transactional
    public Player updateLocation(Long id, Double lat, Double lng) {
        Player existing = getPlayer(id);
        existing.setCurrLat(lat);
        existing.setCurrLong(lng);
        return playerRepository.save(existing);
    }

    private Player getPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", id));
    }

    @Override
    @Transactional
    public Player addFavoriteVenue(Long playerId, Long venueId) {
        Player player = getPlayer(playerId);
        player.getFavoriteVenues().add(venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", venueId)));
        return playerRepository.save(player);
    }

    @Override
    @Transactional
    public Player removeFavoriteVenue(Long playerId, Long venueId) {
        Player player = getPlayer(playerId);
        player.getFavoriteVenues().removeIf(v -> v.getId().equals(venueId));
        return playerRepository.save(player);
    }

    @Override
    @Transactional
    public Player addFavoriteMatch(Long playerId, Long matchId) {
        Player player = getPlayer(playerId);
        player.getFavoriteMatches().add(matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId)));
        return playerRepository.save(player);
    }

    @Override
    @Transactional
    public Player removeFavoriteMatch(Long playerId, Long matchId) {
        Player player = getPlayer(playerId);
        player.getFavoriteMatches().removeIf(m -> m.getId().equals(matchId));
        return playerRepository.save(player);
    }
}
