package ee.taltech.iti03022023salonbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cosmetic")
public class Cosmetic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cosmeticId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;
}
