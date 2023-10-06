package ee.taltech.iti03022023salonbackend.model.service;

import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "service_status")
public class ServiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "service_status")
    private String serviceStatus;

    @OneToMany
    private List<SalonService> serviceList;
}
