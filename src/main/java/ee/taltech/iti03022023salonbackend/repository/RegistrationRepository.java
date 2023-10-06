package ee.taltech.iti03022023salonbackend.repository;

import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.Registration;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findAllByClientAndSalonService(Client client, SalonService service);
}
