package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CosmeticDto {

    private Long cosmeticId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public CosmeticDto(Long cosmeticId, String firstName, String lastName,
                       String phoneNumber, String email) {
        this.cosmeticId = cosmeticId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
