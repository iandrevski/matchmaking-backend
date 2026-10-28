package sportify.matchmakingbackend.service.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportify.matchmakingbackend.dto.AuthResponse;
import sportify.matchmakingbackend.dto.LoginRequest;
import sportify.matchmakingbackend.dto.RegisterRequest;
import sportify.matchmakingbackend.dto.UserDto;
import sportify.matchmakingbackend.model.User;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;
import sportify.matchmakingbackend.service.application.AuthApplicationService;
import sportify.matchmakingbackend.service.domain.TokenService;
import sportify.matchmakingbackend.service.domain.UserService;

@Service
@Transactional(readOnly = true)
public class AuthApplicationServiceImpl implements AuthApplicationService {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthApplicationServiceImpl(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public AuthResponse registerPlayer(RegisterRequest request) {
        // по регистрација корисникот е веднаш најавен (добива токен)
        return toResponse(userService.registerPlayer(request.username(), request.email(), request.password()));
    }

    @Override
    @Transactional
    public AuthResponse registerPartner(RegisterRequest request) {
        return toResponse(userService.registerPartner(request.username(), request.email(), request.password()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return toResponse(userService.authenticate(request.username(), request.password()));
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(tokenService.generateToken(user), UserDto.from(user));
    }

    @Override
    public UserDto me(Long userId) {
        return userService.findById(userId)
                .map(UserDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}