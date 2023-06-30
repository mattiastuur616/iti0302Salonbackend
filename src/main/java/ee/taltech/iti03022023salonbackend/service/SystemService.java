package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.ClientDto;
import ee.taltech.iti03022023salonbackend.dto.CosmeticDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.model.Client;
import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.SalonService;
import ee.taltech.iti03022023salonbackend.model.ServiceType;
import ee.taltech.iti03022023salonbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SystemService {
    private final ClientRepository clientRepository;
    private final CosmeticRepository cosmeticRepository;
    private final RegistrationRepository registrationRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceStatusRepository serviceStatusRepository;
    private final ServiceTypeRepository serviceTypeRepository;


    // Functions with the clients.

    /**
     * Method for showing all the clients existing in the database.
     *
     * @return the list of clients
     */
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clientRepository.findAll()) {
            clientDtoList.add(convertIntoClientDto(client));
        }
        return clientDtoList;
    }

    /**
     * Method for adding a new client to the database.
     * Because of every client has to have their unique email address, function will check
     * if the database doesn't already have a client with the same email.
     *
     * @param client to be added
     * @return the string explaining the result
     */
    public String addClient(Client client) {
        Optional<Client> existingClient = clientRepository.findClientsByEmailIgnoreCase(client.getEmail());
        if (existingClient.isPresent()) {
            return "Client already exists in the database.";
        }
        clientRepository.save(client);
        return "New client " + client.getFirstName() + " " + client.getLastName() + " was successfully added.";
    }

    /**
     * Method for removing the client from the database.
     * Checks if the client even exists before trying to remove them.
     *
     * @param id of the client to be removed
     * @return the string explaining the result
     */
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


    // Functions with the cosmetics.

    /**
     * Method for showing all the cosmetics existing in the database.
     *
     * @return the list of cosmetics
     */
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
    public List<SalonServiceDto> getAllSalonService() {
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
}
