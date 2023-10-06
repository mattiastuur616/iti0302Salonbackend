package ee.taltech.iti03022023salonbackend.controller.roles;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.client.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.client.ClientUserDto;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.service.roles.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class ClientController {
    private final ClientService clientService;


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
     * Get request for client's id.
     *
     * @param email of the client
     * @return client's id
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
    @GetMapping("/allClientUsers")
    public List<ClientUserDto> getAllClientUsers() {return clientService.getAllClientUsers();}

    /**
     * Post request for adding a new client to the salon.
     *
     * @param client to be added
     * @param password of the client
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
     * @param email of the user
     * @param password of the user
     * @return boolean
     */
    @GetMapping("/isValidClient")
    public boolean isValidClient(@RequestParam String email, @RequestParam String password) {
        return clientService.isValidClient(email, password);
    }

    /**
     * Put request for adding money to client's account.
     *
     * @param id of the client
     * @param amount to be added
     */
    @PutMapping("/addMoney")
    public void addMoney(@RequestParam Long id, @RequestParam Integer amount) {
        clientService.addMoney(id, amount);
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
}
