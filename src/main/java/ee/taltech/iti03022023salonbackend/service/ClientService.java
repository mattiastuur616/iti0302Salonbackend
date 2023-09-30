package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.UserDto;
import ee.taltech.iti03022023salonbackend.model.Client;
import ee.taltech.iti03022023salonbackend.model.Registration;
import ee.taltech.iti03022023salonbackend.model.User;
import ee.taltech.iti03022023salonbackend.repository.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.RegistrationRepository;
import ee.taltech.iti03022023salonbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationService registrationService;
    private final ServiceOfServices serviceOfServices;

    /**
     * Method for showing all the clients existing in the database.
     *
     * @return the list of clients
     */
    @Transactional
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clientRepository.findAll()) {
            clientDtoList.add(convertIntoClientDto(client));
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
    public ClientDto getClient(Long id) {
        Optional<Client> existingClient = clientRepository.findById(id);
        return existingClient.map(this::convertIntoClientDto).orElse(null);
    }

    /**
     * Method for receiving client's id.
     *
     * @param email of the client
     * @return client's id
     */
    @Transactional
    public Long getClientId(String email) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            return null;
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
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userDtoList.add(convertIntoUserDto(user));
        }
        return userDtoList;
    }

    /**
     * Method for adding a new client to the database.
     * Because of every client has to have their unique email address, function will check
     * if the database doesn't already have a client with the same email.
     *
     * @param client to be added
     * @return the string explaining the result
     */
    @Transactional
    public String addClient(Client client, String password) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(client.getEmail());
        Optional<User> existingUser = userRepository.findUsersByPasswordIgnoreCase(password);
        if (existingClient.isPresent()) {
            return "Client already exists in the database.";
        } else if (existingUser.isPresent()) {
            return "Password already in use.";
        }
        clientRepository.save(client);
        User newUser = new User();
        newUser.setPassword(password);
        newUser.setClientId(client.getClientId());
        userRepository.save(newUser);
        return client.getFirstName() + " " + client.getLastName();
    }

    /**
     * Method used when logging in.
     * It checks whether the user has put correct credentials.
     *
     * @param email of user
     * @param password of user
     * @return boolean
     */
    public boolean isValidClient(String email, String password) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            return false;
        }
        Client client = existingClient.get();
        Optional<User> existingUser = userRepository.findUsersByClientId(client.getClientId());
        if (existingUser.isEmpty()) {
            return false;
        }
        User user = existingUser.get();
        return user.getPassword().equals(password);
    }

    /**
     * Method for getting client's name
     *
     * @param email of client
     * @return string of client's name
     */
    public String getClientName(String email) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(email);
        if (existingClient.isEmpty()) {
            return "Error";
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
    public String removeClient(Long id) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            return "No such client in the database.";
        }
        Client client = existingClient.get();
        clientRepository.delete(client);
        return "Client " + client.getFirstName() + " " + client.getLastName() + " was removed from the database.";
    }

    /**
     * Method for showing the history of all the services that client has registered.
     *
     * @param id of the client
     * @return the list of services either finished or not canceled by the client
     */
    @Transactional
    public List<SalonServiceDto> getHistoryOfRegisteredServices(Long id) {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            return null;
        }
        Client client = existingClient.get();
        for (Registration registration : registrationRepository.findAll()) {
            if (registration.getClient().getClientId().equals(client.getClientId())) {
                salonServiceDtoList.add(serviceOfServices.convertIntoSalonServiceDto(registration.getSalonService()));
            }
        }
        return salonServiceDtoList;
    }

    /**
     * Help function to convert original client object into the data transfer object.
     *
     * @param client to be converted
     * @return dto of the original client object
     */
    public ClientDto convertIntoClientDto(Client client) {
        return new ClientDto(client.getClientId(), client.getFirstName(), client.getLastName(), client.getMoney(),
                client.getPhoneNumber(), client.getEmail(), client.getIdCode(), client.getDateOfBirth(),
                client.getHomeAddress());
    }

    /**
     * Help function to convert original user object into the data transfer object.
     *
     * @param user to be converted
     * @return dto of the original user object
     */
    public UserDto convertIntoUserDto(User user) {
        return new UserDto(user.getUserId(), user.getClientId(), user.getPassword());
    }
}
