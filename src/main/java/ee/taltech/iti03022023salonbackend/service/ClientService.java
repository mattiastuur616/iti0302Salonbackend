package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.client.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.client.ClientUserDto;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import ee.taltech.iti03022023salonbackend.model.Registration;
import ee.taltech.iti03022023salonbackend.repository.client.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.RegistrationRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientUserRepository;
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
    private final ClientRepository clientRepository;
    private final ClientUserRepository clientUserRepository;
    private final RegistrationRepository registrationRepository;
    private final ServiceOfServices serviceOfServices;

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
    public List<ClientUserDto> getAllUsers() {
        List<ClientUserDto> clientUserDtoList = new ArrayList<>();
        for (ClientUser clientUser : clientUserRepository.findAll()) {
            clientUserDtoList.add(convertIntoUserDto(clientUser));
        }
        return clientUserDtoList;
    }

    /**
     * Method for adding a new client to the database.
     * Because of every client has to have their unique email address, function will check
     * if the database doesn't already have a client with the same email.
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
     * @return status number
     */
    @Transactional
    public String addClient(Client client, String password) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(client.getEmail());
        Optional<ClientUser> existingUser = clientUserRepository.findUsersByPassword(password);
        if (existingClient.isPresent()) {
            return "1";
        } else if (existingUser.isPresent()) {
            return "2";
        } else if (!isValidPassword(password)) {
            return "3";
        } else if (!isValidPhoneNumber(client.getPhoneNumber())) {
            return "4";
        } else if (!isValidIdCode(client.getIdCode())) {
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
     * Control method to check password's validity.
     *
     * @param password to be controlled
     * @return boolean
     */
    public boolean isValidPassword(String password) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Integer counter = 0;
        if (password.length() < 8 || password.length() > 50) {
            return false;
        } else if (password.equals(password.toLowerCase())) {
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            if (alphabet.contains(password.substring(i, i + 1))) {
                counter++;
            }
        }
        return !counter.equals(password.length());
    }

    /**
     * Control method to check validity of phone number.
     *
     * @param number to be controlled
     * @return boolean
     */
    public boolean isValidPhoneNumber(String number) {
        // String numerics = "1234567890";
        List<String> numerics = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        if (number.length() < 7) {
            return false;
        }
        for (int i = 0; i < number.length(); i++) {
            if (!numerics.contains(number.substring(i, i + 1))) {
                return false;
            }
        }
        return true;
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
        Optional<ClientUser> existingUser = clientUserRepository.findUsersByClient(client);
        if (existingUser.isEmpty()) {
            return false;
        }
        ClientUser clientUser = existingUser.get();
        return passwordEncoder.matches(password, clientUser.getPassword());
    }

    /**
     * Method for checking ID code's validity.
     *
     * @param idCode to be checked
     * @return boolean
     */
    public boolean isValidIdCode(String idCode) {
        // List<Integer> months1 = List.of(1, 3, 5, 7, 8, 10, 12);
        // List<Integer> months2 = List.of(4, 6, 9, 11);
        int genderNumber = Integer.parseInt(idCode.substring(0, 1));
        int yearNumber = Integer.parseInt(idCode.substring(1, 3));
        int monthNumber = Integer.parseInt(idCode.substring(3, 5));
        int dayNumber = Integer.parseInt(idCode.substring(5, 7));
        if (idCode.length() != 11) {
            return false;
        } else if (genderNumber > 6 || genderNumber < 1) {
            return false;
        } else if (yearNumber < 0 || yearNumber > 99) {
            return false;
        } else if (monthNumber < 1 || monthNumber > 12) {
            return false;
        } else return dayNumber <= 31 && dayNumber >= 1;
    }

    /**
     * Method for checking control number validity.
     *
     * @param idCode to be checked
     * @return boolean
     */
    public boolean isControlNumberCorrect(String idCode) {
        int one = Integer.parseInt(idCode.substring(0, 1));
        int two = Integer.parseInt(idCode.substring(1, 2));
        int three = Integer.parseInt(idCode.substring(2, 3));
        int four = Integer.parseInt(idCode.substring(3, 4));
        int five = Integer.parseInt(idCode.substring(4, 5));
        int six = Integer.parseInt(idCode.substring(5, 6));
        int seven = Integer.parseInt(idCode.substring(6, 7));
        int eight = Integer.parseInt(idCode.substring(7, 8));
        int nine = Integer.parseInt(idCode.substring(8, 9));
        int ten = Integer.parseInt(idCode.substring(9, 10));
        int last = Integer.parseInt(idCode.substring(10, 11));
        int first_algo = one + two * 2 + three * 3 + four * 4 + five * 5 + six * 6 + seven * 7 + eight * 8 + nine * 9 + ten;
        int second_algo = one * 3 + two * 4 + three * 5 + four * 6 + five * 7 + six * 8 + seven * 9 + eight + nine * 2 + ten * 3;
        if (first_algo % 11 == last) {
            return true;
        } else if (second_algo % 11 == last && first_algo % 11 >= 10) {
            return true;
        } else if (second_algo % 11 >= 10 && last == 0) {
            return true;
        }
        return false;
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
        Optional<ClientUser> existingUser = clientUserRepository.findUsersByClient(client);
        if (existingUser.isEmpty()) {
            return "Error";
        }
        ClientUser clientUser = existingUser.get();
        clientUserRepository.delete(clientUser);
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
     * @param clientUser to be converted
     * @return dto of the original user object
     */
    public ClientUserDto convertIntoUserDto(ClientUser clientUser) {
        return new ClientUserDto(clientUser.getUserId(), clientUser.getClient().getClientId(), clientUser.getPassword());
    }
}
