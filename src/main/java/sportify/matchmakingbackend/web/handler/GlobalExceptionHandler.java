package sportify.matchmakingbackend.web.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sportify.matchmakingbackend.dto.ApiError;
import sportify.matchmakingbackend.model.exceptions.InvalidOperationException;
import sportify.matchmakingbackend.model.exceptions.ResourceNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperation(InvalidOperationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // @Valid на @RequestBody — враќа грешка по поле за да се прикаже во формата
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), status.getReasonPhrase(), "Validation failed", fieldErrors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request body");
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleBadParameter(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // на пр. бришење спорт што се користи во натпревар
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Operation conflicts with existing data");
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), status.getReasonPhrase(), message));
    }
}
