package ee.taltech.iti03022023salonbackend.repository;

import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {
    Optional<SalonService> getSalonServicesByCosmeticAndAndStartingTime(Cosmetic cosmetic, Date startingTime);
}
