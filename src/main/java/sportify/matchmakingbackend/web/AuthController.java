package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sportify.matchmakingbackend.dto.AuthResponse;
import sportify.matchmakingbackend.dto.LoginRequest;
import sportify.matchmakingbackend.dto.RegisterRequest;
import sportify.matchmakingbackend.service.application.AuthApplicationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import sportify.matchmakingbackend.dto.UserDto;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/register/player")
    public ResponseEntity<AuthResponse> registerPlayer(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authApplicationService.registerPlayer(request));
    }

    @PostMapping("/register/partner")
    public ResponseEntity<AuthResponse> registerPartner(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authApplicationService.registerPartner(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authApplicationService.login(request);
    }

    // кој е најавен: Spring го проверил токенот и ни го дава како Jwt
    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal Jwt jwt) {
        return authApplicationService.me(Long.valueOf(jwt.getSubject()));
    }
}