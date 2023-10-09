package ee.taltech.iti03022023salonbackend.model.service;

import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "salon_service")
public class SalonService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "name")
    private String serviceName;

    @Column(name = "price")
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ServiceType serviceType;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "starting_time")
    private Date startingTime;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ServiceStatus serviceStatus;

    @ManyToOne
    @JoinColumn(name = "cosmetic_id")
    private Cosmetic cosmetic;
}
