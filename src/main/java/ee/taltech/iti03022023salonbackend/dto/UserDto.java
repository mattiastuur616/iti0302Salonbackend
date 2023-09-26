package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private Long clientId;
    private String password;

    public UserDto(Long userId, Long clientId, String password) {
        this.userId = userId;
        this.clientId = clientId;
        this.password = password;
    }
}
