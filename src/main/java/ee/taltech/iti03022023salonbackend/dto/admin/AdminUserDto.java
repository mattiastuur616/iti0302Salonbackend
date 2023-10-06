package ee.taltech.iti03022023salonbackend.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserDto {

    private Long userId;
    private Long adminId;
    private String password;

    public AdminUserDto(Long userId, Long adminId, String password) {
        this.userId = userId;
        this.adminId = adminId;
        this.password = password;
    }
}
