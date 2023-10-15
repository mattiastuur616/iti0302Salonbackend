package ee.taltech.iti03022023salonbackend.exception;

public class CannotFindUserException extends Throwable {
    private Role role;
    public enum Role {
        ADMIN,
        COSMETIC,
        CLIENT
    }

    public CannotFindUserException(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
