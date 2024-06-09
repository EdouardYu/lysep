package software.engineering.lysep.service.exception;

public class ValidationCodeException extends RuntimeException {
    public ValidationCodeException(String msg) {
        super(msg);
    }
}
