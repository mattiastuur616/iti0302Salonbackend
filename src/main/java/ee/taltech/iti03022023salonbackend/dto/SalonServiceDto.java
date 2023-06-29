package ee.taltech.iti03022023salonbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class SalonServiceDto {

    private Long serviceId;
    private String name;
    private Integer price;
    private Long typeId;
    private Integer duration;
    private Date startingTime;
    private Long statusId;
    private Long cosmeticId;

    public SalonServiceDto(Long serviceId, String name, Integer price, Long typeId,
                           Integer duration, Date startingTime, Long statusId, Long cosmeticId) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.typeId = typeId;
        this.duration = duration;
        this.startingTime = startingTime;
        this.statusId = statusId;
        this.cosmeticId = cosmeticId;
    }
}
