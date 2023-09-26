package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.*;
import ee.taltech.iti03022023salonbackend.model.*;
import ee.taltech.iti03022023salonbackend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class SystemService {
    private final ClientRepository clientRepository;
    private final CosmeticRepository cosmeticRepository;
    private final RegistrationRepository registrationRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceStatusRepository serviceStatusRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final UserRepository userRepository;


    // Functions with the clients.

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
                salonServiceDtoList.add(convertIntoSalonServiceDto(registration.getSalonService()));
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


    // Functions with the cosmetics.

    /**
     * Method for showing all the cosmetics existing in the database.
     *
     * @return the list of cosmetics
     */
    @Transactional
    public List<CosmeticDto> getAllCosmetics() {
        List<CosmeticDto> cosmeticDtoList = new ArrayList<>();
        for (Cosmetic cosmetic : cosmeticRepository.findAll()) {
            cosmeticDtoList.add(convertIntoCosmeticDto(cosmetic));
        }
        return cosmeticDtoList;
    }

    /**
     * Method for adding a new cosmetic into the database.
     * Because of each one of the cosmetic needs to own unique email address the function check
     * if there isn't already existing cosmetic with that email.
     *
     * @param cosmetic to be added
     * @return the string explaining the result
     */
    @Transactional
    public String addCosmetic(Cosmetic cosmetic) {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findCosmeticsByEmailIgnoreCase(cosmetic.getEmail());
        if (existingCosmetic.isPresent()) {
            return "Cosmetic with this email address already existing in the database";
        }
        cosmeticRepository.save(cosmetic);
        return "New cosmetic " + cosmetic.getFirstName() + " " + cosmetic.getLastName() + " was added into " +
                "the database.";
    }

    /**
     * Method for removing the cosmetic from the database.
     * The function checks if the cosmetic even exists before trying to remove them.
     *
     * @param id of the cosmetic to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeCosmetic(Long id) {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(id);
        if (existingCosmetic.isEmpty()) {
            return "No such cosmetic in the database.";
        }
        Cosmetic cosmetic = existingCosmetic.get();
        cosmeticRepository.delete(cosmetic);
        return "Cosmetic " + cosmetic.getFirstName() + " " + cosmetic.getLastName() + " was removed from " +
                "the database.";
    }

    /**
     * Method for showing every service that is performed by the specific cosmetic.
     *
     * @param id of the cosmetic
     * @return the list of services performed by that cosmetic
     */
    @Transactional
    public List<SalonServiceDto> getAllServicesOfCosmetic(Long id) {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(id);
        if (existingCosmetic.isEmpty()) {
            return null;
        }
        for (SalonService salonService : salonServiceRepository.findAllByCosmetic(existingCosmetic.get())) {
            salonServiceDtoList.add(convertIntoSalonServiceDto(salonService));
        }
        return salonServiceDtoList;
    }

    /**
     * Help function to convert the original object into the data transfer object.
     *
     * @param cosmetic to be converted
     * @return dto of the original cosmetic object
     */
    public CosmeticDto convertIntoCosmeticDto(Cosmetic cosmetic) {
        return new CosmeticDto(cosmetic.getCosmeticId(), cosmetic.getFirstName(), cosmetic.getLastName(),
                cosmetic.getPhoneNumber(), cosmetic.getEmail());
    }


    // Functions with the salon services.

    /**
     * Method for showing all the services existing in the database.
     *
     * @return the list of services
     */
    @Transactional
    public List<SalonServiceDto> getAllSalonServices() {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        for (SalonService salonService : salonServiceRepository.findAll()) {
            salonServiceDtoList.add(convertIntoSalonServiceDto(salonService));
        }
        return salonServiceDtoList;
    }

    /**
     * Method for adding a new service to the database.
     *
     * @param salonService to be added
     * @return the string explaining the result
     */
    @Transactional
    public String addSalonService(SalonService salonService) {
        Optional<SalonService> existingSalonService = salonServiceRepository
                .getSalonServicesByCosmeticAndAndStartingTime(salonService.getCosmetic(), salonService.getStartingTime());
        Optional<ServiceType> existingServiceType = serviceTypeRepository
                .findById(salonService.getServiceType().getTypeId());
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(salonService.getCosmetic().getCosmeticId());
        if (existingSalonService.isPresent()) {
            return "Service is already in the database.";
        } else if (existingServiceType.isEmpty()) {
            return "Can't add the service because there's no such service type in the salon.";
        } else if (existingCosmetic.isEmpty()) {
            return "Can't add the service because there's no such cosmetic in the salon.";
        } else if (salonService.getServiceStatus().getStatusId() != 1) {
            return "Can't add the service because its status isn't available but should.";
        }
        salonServiceRepository.save(salonService);
        return "New service has added to the database.";
    }

    /**
     * Method for removing the service from the database.
     *
     * @param id of the service to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeSalonService(Long id) {
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(id);
        if (existingSalonService.isEmpty()) {
            return "No such service in the database.";
        }
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getServiceStatus().equalsIgnoreCase("finished")) {
            return "The service is finished and will remain as a part of client's registrations history.";
        }
        salonServiceRepository.delete(salonService);
        return "The service with the id " + salonService.getServiceId() + " was removed from the database.";
    }

    /**
     * Help function to convert the original salon service object into the data transfer object.
     *
     * @param salonService to be converted
     * @return dto of the original salon service object
     */
    public SalonServiceDto convertIntoSalonServiceDto(SalonService salonService) {
        return new SalonServiceDto(salonService.getServiceId(), salonService.getName(), salonService.getPrice(),
                salonService.getServiceType().getTypeId(), salonService.getDuration(), salonService.getStartingTime(),
                salonService.getServiceStatus().getStatusId(), salonService.getCosmetic().getCosmeticId());
    }


    // Functions with the registrations.

    /**
     * Method for showing all the registrations in the database.
     *
     * @return the list of registrations
     */
    @Transactional
    public List<RegistrationDto> getAllRegistrations() {
        List<RegistrationDto> registrationDtoList = new ArrayList<>();
        for (Registration registration : registrationRepository.findAll()) {
            registrationDtoList.add(convertIntoRegistrationDto(registration));
        }
        return registrationDtoList;
    }

    /**
     * Method for registering the service for the client.
     *
     * @param clientId of the client who registers the service
     * @param serviceId of the service to be registered
     * @return the string explaining the result
     */
    @Transactional
    public String registerService(Long clientId, Long serviceId) {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            return "No such client in the database.";
        } else if (existingSalonService.isEmpty()) {
            return "No such service in the database.";
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 1) {
            return "Service can't be registered because the service is already busy.";
        } else if (client.getMoney() < salonService.getPrice()) {
            return "Client can't register the service because they don't have enough money.";
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() + 1);
        if (existingServiceStatus.isEmpty()) {
            return "Error with the missing status in the database.";
        }
        ServiceStatus newStatus = existingServiceStatus.get();
        client.setMoney(client.getMoney() - salonService.getPrice());
        salonService.setServiceStatus(newStatus);
        Registration registration = new Registration();
        registration.setSalonService(salonService);
        registration.setClient(client);
        LocalDate now = LocalDate.now();
        registration.setRegistrationDate(Date.valueOf(now));
        registrationRepository.save(registration);
        return "The service has been registered by " + client.getFirstName() + " " + client.getLastName() + ". " +
                "Their cosmetic will be " + salonService.getCosmetic().getFirstName() + " " +
                salonService.getCosmetic().getLastName() + ".";
    }

    /**
     * Method for cancelling the service.
     * Service will be available for registering to the same client or the other clients.
     *
     * @param clientId of the client who cancels the registration
     * @param serviceId of the service to be canceled
     * @return the string explaining the result
     */
    @Transactional
    public String cancelService(Long clientId, Long serviceId) {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            return "No such client in the database.";
        } else if (existingSalonService.isEmpty()) {
            return "No such service in the database.";
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 2) {
            return "Service can't be cancelled because the service is either finished or not registered.";
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() - 1);
        if (existingServiceStatus.isEmpty()) {
            return "Error with the missing status in the database.";
        }
        ServiceStatus newStatus = existingServiceStatus.get();
        client.setMoney(client.getMoney() + salonService.getPrice());
        salonService.setServiceStatus(newStatus);
        Optional<Registration> existingRegistration = registrationRepository
                .findAllByClientAndSalonService(client, salonService);
        if (existingRegistration.isEmpty()) {
            return "Error: can't find the registration in the database.";
        }
        Registration registration = existingRegistration.get();
        registrationRepository.delete(registration);
        return "Registration with the id " + registration.getRegistrationId() + " was canceled in the salon.";
    }

    /**
     * Method for finishing the service, which means the service got performed and client got their wish.
     * After finishing the service, it will no longer be available for registration
     * but remains as the part of the client's registrations history.
     *
     * @param clientId of the client whose registration will be finished
     * @param serviceId of the service to be finished
     * @return the string explaining the result
     */
    @Transactional
    public String finishService(Long clientId, Long serviceId) {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            return "No such client in the database.";
        } else if (existingSalonService.isEmpty()) {
            return "No such service in the database.";
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 2) {
            return "Service can't be finished because the service is either not registered or already finished.";
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() + 1);
        if (existingServiceStatus.isEmpty()) {
            return "Error with the missing status in the database.";
        }
        ServiceStatus newStatus = existingServiceStatus.get();
        salonService.setServiceStatus(newStatus);
        Optional<Registration> existingRegistration = registrationRepository
                .findAllByClientAndSalonService(client, salonService);
        if (existingRegistration.isEmpty()) {
            return "Error: can't find the registration in the database.";
        }
        salonServiceRepository.save(salonService);
        Registration registration = existingRegistration.get();
        return "Service with the id " + registration.getRegistrationId() + " is finished and will remain as part of" +
                " the client " + client.getFirstName() + " " + client.getLastName() + "'s registration history.";
    }

    /**
     * Help function to convert the original registration object into the data transfer object.
     *
     * @param registration to be converted
     * @return dto of the original registration object
     */
    public RegistrationDto convertIntoRegistrationDto(Registration registration) {
        return new RegistrationDto(registration.getRegistrationId(), registration.getSalonService().getServiceId(),
                registration.getClient().getClientId(), registration.getRegistrationDate());
    }
}
