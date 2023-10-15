package ee.taltech.iti03022023salonbackend.exception;

public class ServiceActionException extends Throwable {
    private Action action;
    public enum Action {
        REGISTER,
        CANCEL,
        FINISH
    }

    public ServiceActionException(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
