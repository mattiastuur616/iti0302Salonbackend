package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class RegistrationDto {

    private Long registrationId;
    private Long serviceId;
    private Long clientId;
    private Date registrationDate;

    public RegistrationDto(Long registrationId, Long serviceId,
                           Long clientId, Date registrationDate) {
        this.registrationId = registrationId;
        this.serviceId = serviceId;
        this.clientId = clientId;
        this.registrationDate = registrationDate;
    }
}
