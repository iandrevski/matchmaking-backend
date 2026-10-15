package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.service.domain.PlayerService;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
}
