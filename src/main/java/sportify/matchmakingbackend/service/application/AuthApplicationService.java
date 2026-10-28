package sportify.matchmakingbackend.service.application;

import sportify.matchmakingbackend.dto.AuthResponse;
import sportify.matchmakingbackend.dto.LoginRequest;
import sportify.matchmakingbackend.dto.RegisterRequest;
import sportify.matchmakingbackend.dto.UserDto;

public interface AuthApplicationService {

    AuthResponse registerPlayer(RegisterRequest request);

    AuthResponse registerPartner(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserDto me(Long userId);
}