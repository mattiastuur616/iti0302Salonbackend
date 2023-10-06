package ee.taltech.iti03022023salonbackend.repository.client;

import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    Optional<ClientUser> findUsersByPassword(String password);
    Optional<ClientUser> findUsersByClient(Client client);
}
