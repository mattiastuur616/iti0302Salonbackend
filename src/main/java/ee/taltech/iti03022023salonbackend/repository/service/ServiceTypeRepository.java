package ee.taltech.iti03022023salonbackend.repository.service;

import ee.taltech.iti03022023salonbackend.model.service.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> getServiceTypesByServiceTypeIgnoreCase(String serviceType);
}
