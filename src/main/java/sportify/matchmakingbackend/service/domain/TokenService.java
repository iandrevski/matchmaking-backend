package sportify.matchmakingbackend.service.domain;

import sportify.matchmakingbackend.model.User;

public interface TokenService {

    String generateToken(User user);
}