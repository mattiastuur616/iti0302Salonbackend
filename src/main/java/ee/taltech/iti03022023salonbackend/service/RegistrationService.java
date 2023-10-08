package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.*;
import ee.taltech.iti03022023salonbackend.model.*;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import ee.taltech.iti03022023salonbackend.model.service.ServiceStatus;
import ee.taltech.iti03022023salonbackend.repository.*;
import ee.taltech.iti03022023salonbackend.repository.client.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticUserRepository;
import ee.taltech.iti03022023salonbackend.repository.service.SalonServiceRepository;
import ee.taltech.iti03022023salonbackend.repository.service.ServiceStatusRepository;
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
public class RegistrationService {
    private final ClientRepository clientRepository;
    private final RegistrationRepository registrationRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceStatusRepository serviceStatusRepository;
    private final CosmeticRepository cosmeticRepository;


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
        return "You have successfully registered the service! " +
                "Your cosmetic will be " + salonService.getCosmetic().getFirstName() + " " +
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
        return "Registration is canceled.";
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
        Optional<SalonService> salonServiceOptional = salonServiceRepository.findById(registration.getSalonService().getServiceId());
        Optional<Client> clientOptional = clientRepository.findById(registration.getClient().getClientId());
        if (salonServiceOptional.isEmpty()) {
            return null;
        }
        if (clientOptional.isEmpty()) {
            return null;
        }
        SalonService salonService = salonServiceOptional.get();
        Optional<Cosmetic> cosmeticOptional = cosmeticRepository.findById(salonService.getCosmetic().getCosmeticId());
        if (cosmeticOptional.isEmpty()) {
            return null;
        }
        Cosmetic cosmetic = cosmeticOptional.get();
        Client client = clientOptional.get();
        String cosmeticName = cosmetic.getFirstName() + " " + cosmetic.getLastName();
        String clientName = client.getFirstName() + " " + client.getLastName();
        return new RegistrationDto(registration.getRegistrationId(), salonService.getName(), salonService.getPrice(),
                salonService.getStartingTime(), clientName, cosmeticName, registration.getRegistrationDate());
    }
}
