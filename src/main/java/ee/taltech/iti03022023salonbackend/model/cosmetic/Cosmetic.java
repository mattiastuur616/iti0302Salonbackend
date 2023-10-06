package ee.taltech.iti03022023salonbackend.model.cosmetic;

import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cosmetic")
public class Cosmetic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cosmetic_id")
    private Long cosmeticId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "id_code")
    private String idCode;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "home_address")
    private String homeAddress;

    @OneToMany
    private List<SalonService> serviceList;
}
