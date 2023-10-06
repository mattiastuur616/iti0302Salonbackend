package ee.taltech.iti03022023salonbackend.repository.admin;

import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminsByEmailIgnoreCase(String email);
}
