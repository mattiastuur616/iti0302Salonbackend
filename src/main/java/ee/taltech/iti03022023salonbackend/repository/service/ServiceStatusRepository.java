package ee.taltech.iti03022023salonbackend.repository.service;

import ee.taltech.iti03022023salonbackend.model.service.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {
}
