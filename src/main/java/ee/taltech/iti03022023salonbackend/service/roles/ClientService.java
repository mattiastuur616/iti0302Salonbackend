package ee.taltech.iti03022023salonbackend.service.roles;

import ee.taltech.iti03022023salonbackend.config.ValidityCheck;
import ee.taltech.iti03022023salonbackend.dto.client.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.client.ClientUserDto;
import ee.taltech.iti03022023salonbackend.exception.CannotFindClientException;
import ee.taltech.iti03022023salonbackend.exception.UserMissingException;
import ee.taltech.iti03022023salonbackend.mapper.ServiceMapper;
import ee.taltech.iti03022023salonbackend.mapper.client.ClientMapper;
import ee.taltech.iti03022023salonbackend.mapper.client.ClientUserMapper;
import ee.taltech.iti03022023salonbackend.model.admin.AdminUser;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import ee.taltech.iti03022023salonbackend.model.Registration;
import ee.taltech.iti03022023salonbackend.model.cosmetic.CosmeticUser;
import ee.taltech.iti03022023salonbackend.repository.admin.AdminUserRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.RegistrationRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientUserRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticUserRepository;
import ee.taltech.iti03022023salonbackend.service.ServiceOfServices;
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
public class ClientService {
    private final ValidityCheck validityCheck;
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final ClientUserRepository clientUserRepository;
    @Autowired
    private final CosmeticUserRepository cosmeticUserRepository;
    @Autowired
    private final AdminUserRepository adminUserRepository;
    @Autowired
    private final RegistrationRepository registrationRepository;
    @Autowired
    private final ServiceOfServices serviceOfServices;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ClientUserMapper clientUserMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for showing all the clients existing in the database.
     *
     * @return the list of clients
     */
    @Transactional
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clientRepository.findAll()) {
            clientDtoList.add(clientMapper.clientToClientDto(client));
        }
        return clientDtoList;
    }

    /**
     * Method for getting one client.
     *
     * @param id of the client
     * @return client dto
     */
    @Transactional
    public ClientDto getClient(Long id) throws CannotFindClientException {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            throw  new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        }
        return clientMapper.clientToClientDto(existingClient.get());
    }

    /**
     * Method for receiving client's id.
     *
     * @param email of the client
     * @return client's id
     */
    @Transactional
    public Long getClientId(String email) throws CannotFindClientException {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_EMAIL_FOUND);
        }
        Client actualClient = existingClient.get();
        return actualClient.getClientId();
    }

    /**
     * Method for showing all the users owned by registered clients.
     *
     * @return the list of users
     */
    @Transactional
    public List<ClientUserDto> getAllClientUsers() {
        List<ClientUserDto> clientUserDtoList = new ArrayList<>();
        for (ClientUser clientUser : clientUserRepository.findAll()) {
            clientUserDtoList.add(clientUserMapper.clientUserToClientUserDto(clientUser));
        }
        return clientUserDtoList;
    }

    /**
     * Method for adding a new client to the database.
     * Because of every client has to have their unique email address, function will check
     * if the database doesn't already have a client with the same email.
     *
     * <p>
     * Returns the status number which explain the result:
     * 0 - correct and a new user is registered
     * 1 - client already exists in the database
     * 2 - password already in use
     * 3 - unacceptable password
     * 4 - unacceptable phone number
     * 5 - unacceptable id code
     *
     * @param client to be added
     * @param password of the user
     * @return status number
     */
    @Transactional
    public String addClient(Client client, String password) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(client.getEmail());
        if (existingClient.isPresent()) {
            return "1";
        } else if (passwordExists(password)) {
            return "2";
        } else if (!validityCheck.isValidPassword(password)) {
            return "3";
        } else if (!validityCheck.isValidPhoneNumber(client.getPhoneNumber())) {
            return "4";
        } else if (!validityCheck.isValidIdCode(client.getIdCode())) {
            return "5";
        }
        client.setMoney(0);
        clientRepository.save(client);
        ClientUser newClientUser = new ClientUser();
        newClientUser.setPassword(passwordEncoder.encode(password));
        newClientUser.setClient(client);
        clientUserRepository.save(newClientUser);
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
    public boolean isValidClient(String email, String password) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            return false;
        }
        Client client = existingClient.get();
        Optional<ClientUser> existingUser = clientUserRepository.findUsersByClient(client);
        if (existingUser.isEmpty()) {
            return false;
        }
        ClientUser clientUser = existingUser.get();
        return passwordEncoder.matches(password, clientUser.getPassword());
    }

    /**
     * Method for getting client's name
     *
     * @param email of client
     * @return string of client's name
     */
    @Transactional
    public String getClientName(String email) throws CannotFindClientException {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_EMAIL_FOUND);
        }
        Client actualClient = existingClient.get();
        return actualClient.getFirstName() + " " + actualClient.getLastName();
    }

    /**
     * Method for removing the client from the database.
     * Checks if the client even exists before trying to remove them.
     *
     * @param id of the client to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeClient(Long id) throws CannotFindClientException, UserMissingException {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        }
        Client client = existingClient.get();
        Optional<ClientUser> existingUser = clientUserRepository.findUsersByClient(client);
        if (existingUser.isEmpty()) {
            throw new UserMissingException(UserMissingException.Role.CLIENT);
        }
        ClientUser clientUser = existingUser.get();
        clientUserRepository.delete(clientUser);
        clientRepository.delete(client);
        return "Client " + client.getFirstName() + " " + client.getLastName() + " was removed from the salon.";
    }

    /**
     * Method for showing the history of all the services that client has registered.
     *
     * @param id of the client
     * @return the list of services either finished or not canceled by the client
     */
    @Transactional
    public List<SalonServiceDto> getHistoryOfRegisteredServices(Long id) throws CannotFindClientException {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_EMAIL_FOUND);
        }
        Client client = existingClient.get();
        for (Registration registration : registrationRepository.findAll()) {
            if (registration.getClient().getClientId().equals(client.getClientId())) {
                salonServiceDtoList.add(serviceMapper.serviceToServiceDto(registration.getSalonService()));
            }
        }
        return salonServiceDtoList;
    }

    /**
     * Method for adding money to client's account.
     *
     * @param clientId to find client from database
     * @param amount to be added
     */
    public void addMoney(Long clientId, Integer amount) {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            client.setMoney(client.getMoney() + amount);
            clientRepository.save(client);
        }
    }
}
