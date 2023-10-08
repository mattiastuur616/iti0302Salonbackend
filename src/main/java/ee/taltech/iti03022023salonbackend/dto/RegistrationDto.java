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
    private String clientName;
    private String cosmeticName;
    private Date registrationDate;

    public RegistrationDto(Long registrationId, String serviceName, Integer servicePrice,
                           Date startingTime, String clientName, String cosmeticName,
                           Date registrationDate) {
        this.registrationId = registrationId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.startingTime = startingTime;
        this.clientName = clientName;
        this.cosmeticName = cosmeticName;
        this.registrationDate = registrationDate;
    }
}
