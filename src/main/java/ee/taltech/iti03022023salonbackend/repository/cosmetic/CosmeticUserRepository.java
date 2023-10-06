package ee.taltech.iti03022023salonbackend.repository.cosmetic;

import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.cosmetic.CosmeticUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CosmeticUserRepository extends JpaRepository<CosmeticUser, Long> {
    Optional<CosmeticUser> findCosmeticUsersByPassword(String password);
    Optional<CosmeticUser> findCosmeticUsersByCosmetic(Cosmetic cosmetic);
}
