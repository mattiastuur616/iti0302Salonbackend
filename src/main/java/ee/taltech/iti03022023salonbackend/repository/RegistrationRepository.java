package ee.taltech.iti03022023salonbackend.repository;

import ee.taltech.iti03022023salonbackend.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}