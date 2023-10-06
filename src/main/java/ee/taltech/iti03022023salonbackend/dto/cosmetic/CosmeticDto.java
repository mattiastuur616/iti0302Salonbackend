package ee.taltech.iti03022023salonbackend.dto.cosmetic;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class CosmeticDto {

    private Long cosmeticId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String idCode;
    private Date dateOfBirth;
    private String homeAddress;

    public CosmeticDto(Long cosmeticId, String firstName, String lastName,
                       String phoneNumber, String email, String idCode,
                       Date dateOfBirth, String homeAddress) {
        this.cosmeticId = cosmeticId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.idCode = idCode;
        this.dateOfBirth = dateOfBirth;
        this.homeAddress = homeAddress;
    }
}
