package ee.taltech.iti03022023salonbackend.repository.cosmetic;

import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CosmeticRepository extends JpaRepository<Cosmetic, Long> {
    Optional<Cosmetic> findCosmeticsByEmailIgnoreCase(String email);
}
