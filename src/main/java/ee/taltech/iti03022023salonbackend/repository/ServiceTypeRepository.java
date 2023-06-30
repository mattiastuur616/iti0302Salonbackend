package ee.taltech.iti03022023salonbackend.repository;

import ee.taltech.iti03022023salonbackend.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> getServiceTypesByServiceTypeIgnoreCase(String serviceType);
}
