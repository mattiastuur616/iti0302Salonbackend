package ee.taltech.iti03022023salonbackend.repository.client;

import ee.taltech.iti03022023salonbackend.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientsByEmailIgnoreCase(String email);
}
