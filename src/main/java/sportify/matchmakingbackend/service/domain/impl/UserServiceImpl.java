package sportify.matchmakingbackend.service.domain.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.model.Partner;
import sportify.matchmakingbackend.model.Player;
import sportify.matchmakingbackend.model.User;
import sportify.matchmakingbackend.model.exceptions.InvalidCredentialsException;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.repository.PartnerRepository;
import sportify.matchmakingbackend.repository.PlayerRepository;
import sportify.matchmakingbackend.repository.UserRepository;
import sportify.matchmakingbackend.service.domain.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PlayerRepository playerRepository,
                           PartnerRepository partnerRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.partnerRepository = partnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Player registerPlayer(String username, String email, String rawPassword) {
        checkAvailable(username, email);
        // лозинката се чува само како BCrypt хаш
        return playerRepository.save(new Player(username, email, passwordEncoder.encode(rawPassword)));
    }

    @Override
    @Transactional
    public Partner registerPartner(String username, String email, String rawPassword) {
        checkAvailable(username, email);
        return partnerRepository.save(new Partner(username, email, passwordEncoder.encode(rawPassword)));
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        // иста порака и за непостоечки корисник и за погрешна лозинка,
        // за да не се открива кои кориснички имиња постојат
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return user;
    }

    private void checkAvailable(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidOperationException("Username '" + username + "' is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new InvalidOperationException("Email '" + email + "' is already registered");
        }
    }
}