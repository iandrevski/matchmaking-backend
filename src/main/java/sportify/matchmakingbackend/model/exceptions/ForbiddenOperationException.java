package sportify.matchmakingbackend.model.exceptions;

// најавен си, но ова не е твое (туѓ натпревар, туѓ терен) → 403
public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}