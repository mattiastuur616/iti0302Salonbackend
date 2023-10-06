package ee.taltech.iti03022023salonbackend.model.cosmetic;

import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cosmetic_users")
public class CosmeticUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "cosmetic_id")
    private Cosmetic cosmetic;

    @Column(name = "password")
    private String password;
}
