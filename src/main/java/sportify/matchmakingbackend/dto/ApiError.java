package sportify.matchmakingbackend.dto;

import java.time.LocalDateTime;
import java.util.Map;

// униформен формат на грешка за клиентот (React Native)
public record ApiError(
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp
) {

    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, Map.of(), LocalDateTime.now());
    }

    public static ApiError of(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ApiError(status, error, message, fieldErrors, LocalDateTime.now());
    }
}
