package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.dto.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.CosmeticDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.UserDto;
import ee.taltech.iti03022023salonbackend.model.Client;
import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.SalonService;
import ee.taltech.iti03022023salonbackend.service.ClientService;
import ee.taltech.iti03022023salonbackend.service.CosmeticService;
import ee.taltech.iti03022023salonbackend.service.ServiceOfServices;
import ee.taltech.iti03022023salonbackend.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class SystemController {
    private final RegistrationService registrationService;
    private final ClientService clientService;
    private final CosmeticService cosmeticService;
    private final ServiceOfServices serviceOfServices;


    // Requests of the clients.

    /**
     * Get request for showing all the clients in the salon system.
     *
     * @return the list of clients
     */
    @GetMapping("/allClients")
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients();
    }

    /**
     * Get request of one client.
     *
     * @param id of the client
     * @return client dto
     */
    @GetMapping("/client/{id}")
    public ClientDto getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    /**
     * Get request for showing one client's data.
     *
     * @param email of the client
     * @return client dto
     */
    @GetMapping("/getClient")
    public Long getClientId(@RequestParam String email) {
        return clientService.getClientId(email);
    }

    /**
     * Get request for showing all the users owned by the clients.
     *
     * @return the list of users
     */
    @GetMapping("/allUsers")
    public List<UserDto> getAllUsers() {return clientService.getAllUsers();}

    /**
     * Post request for adding a new client to the salon.
     *
     * @param client to be added
     * @return the string explaining the result
     */
    @PostMapping("/addClient")
    public String addClient(@RequestBody Client client, @RequestParam String password) {
        return clientService.addClient(client, password);
    }

    /**
     * Get request for showing client's name.
     *
     * @param email of the client
     * @return string of full name
     */
    @GetMapping("/clientName")
    public String getClientName(@RequestParam String email) {
        return clientService.getClientName(email);
    }

    /**
     * Get request for user to log in.
     *
     * @param email email
     * @param password password
     * @return boolean
     */
    @GetMapping("/isValid")
    public boolean isValidClient(@RequestParam String email, @RequestParam String password) {
        return clientService.isValidClient(email, password);
    }

    /**
     * Delete request for removing the client from the salon.
     *
     * @param id of the client to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeClient/{id}")
    public String removeClient(@PathVariable Long id) {
        return clientService.removeClient(id);
    }

    /**
     * Get request for showing all the services that client had registered
     * and are either finished or not cancelled yet.
     *
     * @param id of the client
     * @return the list of services
     */
    @GetMapping("/getHistory/{id}")
    public List<SalonServiceDto> getHistoryOfRegisteredServices(@PathVariable Long id) {
        return clientService.getHistoryOfRegisteredServices(id);
    }


    // Requests of the cosmetics.

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
     * Post request for adding a new cosmetic to the salon.
     *
     * @param cosmetic to be added
     * @return the string explaining the result
     */
    @PostMapping("/addCosmetic")
    public String addCosmetic(@RequestBody Cosmetic cosmetic) {
        return cosmeticService.addCosmetic(cosmetic);
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


    // Requests of the salon services.

    /**
     * Get request for showing all the services in the salon.
     *
     * @return the list of services
     */
    @GetMapping("/allServices")
    public List<SalonServiceDto> getAllSalonServices() {
        return serviceOfServices.getAllSalonServices();
    }

    /**
     * Get request for showing all available services.
     *
     * @return the list of available services
     */
    @GetMapping("/availableServices")
    public List<SalonServiceDto> getAvailableSalonServices() {
        return serviceOfServices.getAvailableSalonServices();
    }

    /**
     * Get request for getting data about one specific service.
     *
     * @param id of the service
     * @return dto of the service
     */
    @GetMapping("/service/{id}")
    public SalonServiceDto getSalonServiceById(@PathVariable Long id) {
        return serviceOfServices.getSalonServiceById(id);
    }

    /**
     * Post request for adding a new service to the salon.
     *
     * @param salonService to be added
     * @return the string explaining the result
     */
    @PostMapping("/addService")
    public String addSalonService(@RequestBody SalonService salonService) {
        return serviceOfServices.addSalonService(salonService);
    }

    /**
     * Delete request for removing the service from the salon.
     *
     * @param id of the service to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeService/{id}")
    public String removeSalonService(@PathVariable Long id) {
        return serviceOfServices.removeSalonService(id);
    }


    // Requests of the registrations.

    /**
     * Post request for registering a service for the client.
     *
     * @param clientId of the client who registers the service
     * @param serviceId of the service to be registered
     * @return the string explaining the result
     */
    @PostMapping("/registerService")
    public String registerService(@RequestParam Long clientId, @RequestParam Long serviceId) {
        return registrationService.registerService(clientId, serviceId);
    }

    /**
     * Delete request for canceling the registration.
     *
     * @param clientId of the client who cancels the service
     * @param serviceId of the service to be canceled
     * @return the string explaining the result
     */
    @DeleteMapping("/cancelService")
    public String cancelService(@RequestParam Long clientId, @RequestParam Long serviceId) {
        return registrationService.cancelService(clientId, serviceId);
    }

    /**
     * Put request for finishing the registration.
     *
     * @param clientId of the client whose registration will be finished
     * @param serviceId of the service to be finished
     * @return the string explaining the result
     */
    @PutMapping("/finishService")
    public String finishService(@RequestParam Long clientId, @RequestParam Long serviceId) {
        return registrationService.finishService(clientId, serviceId);
    }
}
