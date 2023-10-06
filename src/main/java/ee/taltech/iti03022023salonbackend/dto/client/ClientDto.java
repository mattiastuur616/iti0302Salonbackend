package ee.taltech.iti03022023salonbackend.dto.client;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ClientDto {

    private Long clientId;
    private String firstName;
    private String lastName;
    private Integer money;
    private String phoneNumber;
    private String email;
    private String idCode;
    private Date dateOfBirth;
    private String homeAddress;

    public ClientDto(Long clientId, String firstName, String lastName, Integer money,
                     String phoneNumber, String email, String idCode, Date dateOfBirth,
                     String homeAddress) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.money = money;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.idCode = idCode;
        this.dateOfBirth = dateOfBirth;
        this.homeAddress = homeAddress;
    }
}
