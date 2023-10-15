package ee.taltech.iti03022023salonbackend.exception;

public class CannotFindAdminException extends Throwable {
    public Reason reason;
    public enum Reason {
        NO_ID_FOUND,
        NO_EMAIL_FOUND
    }

    public CannotFindAdminException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
