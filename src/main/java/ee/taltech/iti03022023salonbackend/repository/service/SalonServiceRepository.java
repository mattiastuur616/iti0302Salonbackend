package ee.taltech.iti03022023salonbackend.repository.service;

import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {
    Optional<SalonService> getSalonServicesByCosmeticAndAndStartingTime(Cosmetic cosmetic, Date startingTime);
    List<SalonService> findAllByCosmetic(Cosmetic cosmetic);
}
