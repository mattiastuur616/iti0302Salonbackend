package ee.taltech.iti03022023salonbackend.model.admin;

import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_users")
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "password")
    private String password;
}
