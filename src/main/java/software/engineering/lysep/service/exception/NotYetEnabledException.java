package software.engineering.lysep.service.exception;

public class NotYetEnabledException extends RuntimeException {
    public NotYetEnabledException(String msg) {
        super(msg);
    }
}
