package ee.taltech.iti03022023salonbackend.repository;

import ee.taltech.iti03022023salonbackend.model.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {
}
