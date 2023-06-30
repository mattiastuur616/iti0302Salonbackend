package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.dto.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.CosmeticDto;
import ee.taltech.iti03022023salonbackend.model.Client;
import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SystemController {
    private final SystemService systemService;

    /**
     * Get request for showing all the clients in the salon system.
     *
     * @return the list of clients
     */
    @GetMapping("/allClients")
    public List<ClientDto> getAllClients() {
        return systemService.getAllClients();
    }

    /**
     * Post request for adding a new client to the salon.
     *
     * @param client to be added
     * @return the string explaining the result
     */
    @PostMapping("/addClient")
    public String addClient(@RequestBody Client client) {
        return systemService.addClient(client);
    }

    /**
     * Delete request for removing the client from the salon.
     *
     * @param id of the client to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeClient/{id}")
    public String removeClient(@PathVariable Long id) {
        return systemService.removeClient(id);
    }

    /**
     * Get request for showing all the cosmetics in the salon system.
     *
     * @return the list of cosmetics
     */
    @GetMapping("/allCosmetics")
    public List<CosmeticDto> getAllCosmetics() {
        return systemService.getAllCosmetics();
    }

    /**
     * Post request for adding a new cosmetic to the salon.
     *
     * @param cosmetic to be added
     * @return the string explaining the result
     */
    @PostMapping("/addCosmetic")
    public String addCosmetic(@RequestBody Cosmetic cosmetic) {
        return systemService.addCosmetic(cosmetic);
    }

    /**
     * Delete request for removing the cosmetic from the salon.
     *
     * @param id of the cosmetic to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeCosmetic/{id}")
    public String removeCosmetic(@PathVariable Long id) {
        return systemService.removeCosmetic(id);
    }
}
