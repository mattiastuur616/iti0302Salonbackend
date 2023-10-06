package ee.taltech.iti03022023salonbackend.model.client;

import ee.taltech.iti03022023salonbackend.model.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class ClientUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "password")
    private String password;
}
