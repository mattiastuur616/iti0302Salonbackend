package ee.taltech.iti03022023salonbackend.controller.roles;

import ee.taltech.iti03022023salonbackend.dto.admin.AdminDto;
import ee.taltech.iti03022023salonbackend.dto.admin.AdminUserDto;
import ee.taltech.iti03022023salonbackend.exception.CannotFindAdminException;
import ee.taltech.iti03022023salonbackend.exception.CannotFindUserException;
import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import ee.taltech.iti03022023salonbackend.service.roles.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class AdminController {
    @Autowired
    private final AdminService adminService;


    /**
     * Get request for showing all the admins in the salon system.
     *
     * @return the list of admins
     */
    @GetMapping("/allAdmins")
    public List<AdminDto> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    /**
     * Get request of one admin.
     *
     * @param id of the admin
     * @return admin dto
     */
    @GetMapping("/admin/{id}")
    public AdminDto getAdmin(@PathVariable Long id) throws CannotFindAdminException {
        return adminService.getAdmin(id);
    }

    /**
     * Get request for admin id.
     *
     * @param email of the admin
     * @return admin id
     */
    @GetMapping("/getAdmin")
    public Long getAdminId(@RequestParam String email) throws CannotFindAdminException {
        return adminService.getAdminId(email);
    }

    /**
     * Get request for showing all the users owned by the admins.
     *
     * @return the list of admins
     */
    @GetMapping("/allAdminUsers")
    public List<AdminUserDto> getAllAdminUsers() {
        return adminService.getAllAdminUsers();
    }

    /**
     * Post request for adding a new admin to the salon.
     *
     * @param admin to be added
     * @param password of the admin
     * @return the string explaining the result
     */
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody Admin admin, @RequestParam String password) {
        return adminService.addAdmin(admin, password);
    }

    /**
     * Get request for showing admin name.
     *
     * @param email of the admin
     * @return string of full name
     */
    @GetMapping("/adminName")
    public String getAdminName(@RequestParam String email) throws CannotFindAdminException {
        return adminService.getAdminName(email);
    }

    /**
     * Get request for user to log in.
     *
     * @param email of the user
     * @param password of the user
     * @return boolean
     */
    @GetMapping("/isValidAdmin")
    public boolean isValidAdmin(@RequestParam String email, @RequestParam String password) {
        return adminService.isValidAdmin(email, password);
    }

    /**
     * Delete request for removing the admin from the salon.
     *
     * @param id of the admin to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeAdmin/{id}")
    public String removeAdmin(@PathVariable Long id) throws CannotFindAdminException, CannotFindUserException {
        return adminService.removeAdmin(id);
    }
}
