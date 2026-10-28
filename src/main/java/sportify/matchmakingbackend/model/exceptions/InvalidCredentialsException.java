package sportify.matchmakingbackend.model.exceptions;

// погрешно корисничко име или лозинка → 401
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}