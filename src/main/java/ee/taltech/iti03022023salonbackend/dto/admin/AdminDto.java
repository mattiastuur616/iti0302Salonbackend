package ee.taltech.iti03022023salonbackend.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AdminDto {

    private Long adminId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String idCode;
    private Date dateOfBirth;
    private String homeAddress;

    public AdminDto(Long adminId, String firstName, String lastName, Integer money,
                    String phoneNumber, String email, String idCode, Date dateOfBirth,
                    String homeAddress) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.idCode = idCode;
        this.dateOfBirth = dateOfBirth;
        this.homeAddress = homeAddress;
    }
}
