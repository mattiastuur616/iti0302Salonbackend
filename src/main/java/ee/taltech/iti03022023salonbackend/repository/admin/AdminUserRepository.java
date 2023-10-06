package ee.taltech.iti03022023salonbackend.repository.admin;

import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import ee.taltech.iti03022023salonbackend.model.admin.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findAdminUsersByPassword(String password);
    Optional<AdminUser> findAdminUserByAdmin(Admin admin);
}
