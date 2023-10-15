package ee.taltech.iti03022023salonbackend.exception;

public class CannotFindCosmeticException extends Throwable {
    private Reason reason;
    public enum Reason {
        NO_ID_FOUND,
        NO_EMAIL_FOUND
    }

    public CannotFindCosmeticException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
