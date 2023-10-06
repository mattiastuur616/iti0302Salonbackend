package ee.taltech.iti03022023salonbackend.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUserDto {

    private Long userId;
    private Long clientId;
    private String password;

    public ClientUserDto(Long userId, Long clientId, String password) {
        this.userId = userId;
        this.clientId = clientId;
        this.password = password;
    }
}
