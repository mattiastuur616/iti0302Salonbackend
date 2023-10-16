package ee.taltech.iti03022023salonbackend.exception;

public class UserMissingException extends Throwable {
    private Role role;
    public enum Role {
        ADMIN,
        COSMETIC,
        CLIENT
    }

    public UserMissingException(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
