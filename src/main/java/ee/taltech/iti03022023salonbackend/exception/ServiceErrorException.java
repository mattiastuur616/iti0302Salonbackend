package ee.taltech.iti03022023salonbackend.exception;

public class ServiceErrorException extends Throwable {

    private Reason reason;
    public enum Reason {
        SERVICE_EXISTS,
        WRONG_TYPE,
        WRONG_STATUS
    }

    public ServiceErrorException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
