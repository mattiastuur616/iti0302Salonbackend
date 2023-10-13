package ee.taltech.iti03022023salonbackend.service.roles;

import ee.taltech.iti03022023salonbackend.config.ValidityCheck;
import ee.taltech.iti03022023salonbackend.dto.admin.AdminDto;
import ee.taltech.iti03022023salonbackend.dto.admin.AdminUserDto;
import ee.taltech.iti03022023salonbackend.mapper.admin.AdminMapper;
import ee.taltech.iti03022023salonbackend.mapper.admin.AdminUserMapper;
import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import ee.taltech.iti03022023salonbackend.model.admin.AdminUser;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import ee.taltech.iti03022023salonbackend.model.cosmetic.CosmeticUser;
import ee.taltech.iti03022023salonbackend.repository.admin.AdminRepository;
import ee.taltech.iti03022023salonbackend.repository.admin.AdminUserRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientUserRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ValidityCheck validityCheck;
    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final AdminUserRepository adminUserRepository;
    @Autowired
    private final ClientUserRepository clientUserRepository;
    @Autowired
    private final CosmeticUserRepository cosmeticUserRepository;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for showing all the admins existing in the database.
     *
     * @return the list of admins
     */
    @Transactional
    public List<AdminDto> getAllAdmins() {
        List<AdminDto> adminDtoList = new ArrayList<>();
        for (Admin admin : adminRepository.findAll()) {
            adminDtoList.add(adminMapper.adminToAdminDto(admin));
        }
        return adminDtoList;
    }

    /**
     * Method for getting one admin.
     *
     * @param id of the admin
     * @return admin dto
     */
    @Transactional
    public AdminDto getAdmin(Long id) {
        Optional<Admin> existingAdmin = adminRepository.findById(id);
        return existingAdmin.map(admin -> adminMapper.adminToAdminDto(admin)).orElse(null);
    }

    /**
     * Method for receiving admin id.
     *
     * @param email of the admin
     * @return admin id
     */
    @Transactional
    public Long getAdminId(String email) {
        Optional<Admin> existingAdmin = adminRepository.findAdminsByEmailIgnoreCase(email);
        if (existingAdmin.isEmpty()) {
            return null;
        }
        Admin actualAdmin = existingAdmin.get();
        return actualAdmin.getAdminId();
    }

    /**
     * Method for showing all the users owned by salon admins.
     *
     * @return the list of users
     */
    @Transactional
    public List<AdminUserDto> getAllAdminUsers() {
        List<AdminUserDto> adminUserDtoList = new ArrayList<>();
        for (AdminUser adminUser : adminUserRepository.findAll()) {
            adminUserDtoList.add(adminUserMapper.adminUserToAdminUserDto(adminUser));
        }
        return adminUserDtoList;
    }

    /**
     * Method for adding a new admin to the database.
     * Because of every admin has to have their unique email address, function will check
     * if the database doesn't already have an admin with the same email.
     *
     * <p>
     * Returns the status number which explain the result:
     * 0 - correct and a new user is registered
     * 1 - admin already exists in the database
     * 2 - password already in use
     * 3 - unacceptable password
     * 4 - unacceptable phone number
     * 5 - unacceptable id code
     *
     * @param admin to be added
     * @param password of the user
     * @return status number
     */
    @Transactional
    public String addAdmin(Admin admin, String password) {
        Optional<Admin> existingAdmin = adminRepository.findAdminsByEmailIgnoreCase(admin.getEmail());
        if (existingAdmin.isPresent()) {
            return "1";
        } else if (passwordExists(password)) {
            return "2";
        } else if (!validityCheck.isValidPassword(password)) {
            return "3";
        } else if (!validityCheck.isValidPhoneNumber(admin.getPhoneNumber())) {
            return "4";
        } else if (!validityCheck.isValidIdCode(admin.getIdCode())) {
            return "5";
        }
        adminRepository.save(admin);
        AdminUser newAdminUser = new AdminUser();
        newAdminUser.setPassword(passwordEncoder.encode(password));
        newAdminUser.setAdmin(admin);
        adminUserRepository.save(newAdminUser);
        return "0";
    }

    /**
     * Function to check if entered password doesn't already exist.
     *
     * @param password entered by the new user
     * @return boolean
     */
    public boolean passwordExists(String password) {
        List<String> passwords = new ArrayList<>();
        for (CosmeticUser user : cosmeticUserRepository.findAll()) {
            passwords.add(user.getPassword());
        }
        for (ClientUser clientUser : clientUserRepository.findAll()) {
            passwords.add(clientUser.getPassword());
        }
        for (AdminUser adminUser : adminUserRepository.findAll()) {
            passwords.add(adminUser.getPassword());
        }
        for (String pass : passwords) {
            if (passwordEncoder.matches(password, pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method used when logging in.
     * It checks whether the user has put correct credentials.
     *
     * @param email of user
     * @param password of user
     * @return boolean
     */
    @Transactional
    public boolean isValidAdmin(String email, String password) {
        Optional<Admin> existingAdmin = adminRepository.findAdminsByEmailIgnoreCase(email);
        if (existingAdmin.isEmpty()) {
            return false;
        }
        Admin admin = existingAdmin.get();
        Optional<AdminUser> existingUser = adminUserRepository.findAdminUserByAdmin(admin);
        if (existingUser.isEmpty()) {
            return false;
        }
        AdminUser adminUser = existingUser.get();
        return passwordEncoder.matches(password, adminUser.getPassword());
    }

    /**
     * Method for getting admin name
     *
     * @param email of admin
     * @return string of admin name
     */
    @Transactional
    public String getAdminName(String email) {
        Optional<Admin> existingAdmin = adminRepository.findAdminsByEmailIgnoreCase(email);
        if (existingAdmin.isEmpty()) {
            return "Error";
        }
        Admin actualAdmin = existingAdmin.get();
        return actualAdmin.getFirstName() + " " + actualAdmin.getLastName();
    }

    /**
     * Method for removing the admin from the database.
     * Checks if the admin even exists before trying to remove them.
     *
     * @param id of the admin to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeAdmin(Long id) {
        Optional<Admin> existingAdmin = adminRepository.findById(id);
        if (existingAdmin.isEmpty()) {
            return "No such admin in the database.";
        }
        Admin admin = existingAdmin.get();
        Optional<AdminUser> existingUser = adminUserRepository.findAdminUserByAdmin(admin);
        if (existingUser.isEmpty()) {
            return "Error";
        }
        AdminUser adminUser = existingUser.get();
        adminUserRepository.delete(adminUser);
        adminRepository.delete(admin);
        return "Admin " + admin.getFirstName() + " " + admin.getLastName() + " was removed from the database.";
    }
}
