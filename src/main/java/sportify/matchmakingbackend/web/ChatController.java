package sportify.matchmakingbackend.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sportify.matchmakingbackend.dto.MessageDto;
import sportify.matchmakingbackend.dto.SendMessageDto;
import sportify.matchmakingbackend.service.application.ChatApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/matches/{matchId}/messages")
public class ChatController {

    private final ChatApplicationService chatApplicationService;

    public ChatController(ChatApplicationService chatApplicationService) {
        this.chatApplicationService = chatApplicationService;
    }

    // GET ...?after=42 → само пораките по 42 (апликацијата прашува на секои 3 секунди)
    @GetMapping
    public List<MessageDto> findMessages(@PathVariable Long matchId,
                                         @RequestParam(required = false) Long after,
                                         @AuthenticationPrincipal Jwt jwt) {
        return chatApplicationService.findMessages(matchId, userId(jwt), after);
    }

    @PostMapping
    public ResponseEntity<MessageDto> send(@PathVariable Long matchId, @AuthenticationPrincipal Jwt jwt,
                                           @Valid @RequestBody SendMessageDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatApplicationService.send(matchId, userId(jwt), dto.text()));
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}