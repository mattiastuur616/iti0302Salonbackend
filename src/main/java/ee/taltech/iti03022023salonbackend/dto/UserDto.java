package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String password;

    public UserDto(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
