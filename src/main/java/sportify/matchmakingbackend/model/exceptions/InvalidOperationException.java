package sportify.matchmakingbackend.model.exceptions;

// прекршено бизнис правило (полн натпревар, зафатен термин...)
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
