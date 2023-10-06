package ee.taltech.iti03022023salonbackend.controller.roles;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticDto;
import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticUserDto;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.service.roles.CosmeticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class CosmeticController {
    private final CosmeticService cosmeticService;


    /**
     * Get request for showing all the cosmetics in the salon system.
     *
     * @return the list of cosmetics
     */
    @GetMapping("/allCosmetics")
    public List<CosmeticDto> getAllCosmetics() {
        return cosmeticService.getAllCosmetics();
    }

    /**
     * Get request for showing data about one cosmetic.
     *
     * @param id of the cosmetic
     * @return cosmetic dto
     */
    @GetMapping("/cosmetic/{id}")
    public CosmeticDto getCosmetic(@PathVariable Long id) {
        return cosmeticService.getCosmetic(id);
    }

    /**
     * Get request for cosmetic's id.
     *
     * @param email of the cosmetic
     * @return cosmetic's id
     */
    @GetMapping("/getCosmetic")
    public Long getCosmeticId(@RequestParam String email) {
        return cosmeticService.getCosmeticId(email);
    }

    /**
     * Get request for showing all the users owned by the cosmetics.
     *
     * @return the list of users
     */
    @GetMapping("/getAllCosmeticUsers")
    public List<CosmeticUserDto> getAllCosmeticUsers() {
        return cosmeticService.getAllCosmeticUsers();
    }

    /**
     * Post request for adding a new cosmetic to the salon.
     *
     * @param cosmetic to be added
     * @return the string explaining the result
     */
    @PostMapping("/addCosmetic")
    public String addCosmetic(@RequestBody Cosmetic cosmetic, @RequestParam String password) {
        return cosmeticService.addCosmetic(cosmetic, password);
    }

    /**
     * Get request for user to log in.
     *
     * @param email email
     * @param password password
     * @return boolean
     */
    @GetMapping("/isValidCosmetic")
    public boolean isValidCosmetic(@RequestParam String email, @RequestParam String password) {
        return cosmeticService.isValidCosmetic(email, password);
    }

    /**
     * Get request for showing cosmetic's name.
     *
     * @param email of the cosmetic
     * @return string of full name
     */
    @GetMapping("/cosmeticName")
    public String getCosmeticName(@RequestParam String email) {
        return cosmeticService.getCosmeticName(email);
    }

    /**
     * Delete request for removing the cosmetic from the salon.
     *
     * @param id of the cosmetic to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeCosmetic/{id}")
    public String removeCosmetic(@PathVariable Long id) {
        return cosmeticService.removeCosmetic(id);
    }

    /**
     * Get request for showing every service one cosmetic is about to be part of.
     *
     * @param id of the cosmetic
     * @return the list of services
     */
    @GetMapping("/allTasks/{id}")
    public List<SalonServiceDto> getAllServicesOfCosmetic(@PathVariable Long id) {
        return cosmeticService.getAllServicesOfCosmetic(id);
    }
}
