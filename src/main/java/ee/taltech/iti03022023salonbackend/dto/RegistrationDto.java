package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class RegistrationDto {

    private Long registrationId;
    private String serviceName;
    private Integer servicePrice;
    private Date startingTime;
    private String clientFirstName;
    private String clientLastName;
    private String cosmeticFirstName;
    private String cosmeticLastName;
    private Date registrationDate;

    public RegistrationDto(Long registrationId, String serviceName, Integer servicePrice,
                           Date startingTime, String clientFirstName, String clientLastName,
                           String cosmeticFirstName, String cosmeticLastName, Date registrationDate) {
        this.registrationId = registrationId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.startingTime = startingTime;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.cosmeticFirstName = cosmeticFirstName;
        this.cosmeticLastName = cosmeticLastName;
        this.registrationDate = registrationDate;
    }
}
