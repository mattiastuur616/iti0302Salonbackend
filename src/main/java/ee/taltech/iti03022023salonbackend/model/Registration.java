package ee.taltech.iti03022023salonbackend.model;

import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private SalonService salonService;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "registration_date")
    private Date registrationDate;
}
