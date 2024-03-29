package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.*;
import ee.taltech.iti03022023salonbackend.exception.*;
import ee.taltech.iti03022023salonbackend.mapper.RegistrationMapper;
import ee.taltech.iti03022023salonbackend.model.*;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import ee.taltech.iti03022023salonbackend.model.service.ServiceStatus;
import ee.taltech.iti03022023salonbackend.repository.*;
import ee.taltech.iti03022023salonbackend.repository.client.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.service.SalonServiceRepository;
import ee.taltech.iti03022023salonbackend.repository.service.ServiceStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class RegistrationService {
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final RegistrationRepository registrationRepository;
    @Autowired
    private final SalonServiceRepository salonServiceRepository;
    @Autowired
    private final ServiceStatusRepository serviceStatusRepository;
    @Autowired
    private RegistrationMapper registrationMapper;


    /**
     * Method for showing all the registrations in the database.
     *
     * @return the list of registrations
     */
    @Transactional
    public List<RegistrationDto> getAllRegistrations() {
        List<RegistrationDto> registrationDtoList = new ArrayList<>();
        for (Registration registration : registrationRepository.findAll()) {
            registrationDtoList.add(registrationMapper.registrationToRegistrationDto(registration));
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
    public String registerService(Long clientId, Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindStatusException {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        } else if (existingSalonService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 1) {
            throw new ServiceActionException(ServiceActionException.Action.REGISTER);
        } else if (client.getMoney() < salonService.getPrice()) {
            throw new ServiceActionException(ServiceActionException.Action.REGISTER);
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() + 1);
        if (existingServiceStatus.isEmpty()) {
            throw new CannotFindStatusException();
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
    public String cancelService(Long clientId, Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindRegistrationException,
            CannotFindStatusException {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        } else if (existingSalonService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 2) {
            throw new ServiceActionException(ServiceActionException.Action.CANCEL);
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() - 1);
        if (existingServiceStatus.isEmpty()) {
            throw new CannotFindStatusException();
        }
        ServiceStatus newStatus = existingServiceStatus.get();
        client.setMoney(client.getMoney() + salonService.getPrice());
        salonService.setServiceStatus(newStatus);
        Optional<Registration> existingRegistration = registrationRepository
                .findAllByClientAndSalonService(client, salonService);
        if (existingRegistration.isEmpty()) {
            throw new CannotFindRegistrationException();
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
    public String finishService(Long clientId, Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindRegistrationException,
            CannotFindStatusException {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        } else if (existingSalonService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        if (salonService.getServiceStatus().getStatusId() != 2) {
            throw new ServiceActionException(ServiceActionException.Action.FINISH);
        }
        Optional<ServiceStatus> existingServiceStatus = serviceStatusRepository
                .findById(salonService.getServiceStatus().getStatusId() + 1);
        if (existingServiceStatus.isEmpty()) {
            throw new CannotFindStatusException();
        }
        ServiceStatus newStatus = existingServiceStatus.get();
        salonService.setServiceStatus(newStatus);
        Optional<Registration> existingRegistration = registrationRepository
                .findAllByClientAndSalonService(client, salonService);
        if (existingRegistration.isEmpty()) {
            throw new CannotFindRegistrationException();
        }
        salonServiceRepository.save(salonService);
        Registration registration = existingRegistration.get();
        return "Service with the id " + registration.getRegistrationId() + " is finished and will remain as part of" +
                " the client " + client.getFirstName() + " " + client.getLastName() + "'s registration history.";
    }

    /**
     * Method for removing both the registration and a service itself.
     *
     * @param clientId of the client
     * @param serviceId of the service
     * @return string
     */
    @Transactional
    public String removeServiceAndRegistration(Long clientId, Long serviceId) throws CannotFindClientException,
            CannotFindServiceException, CannotFindRegistrationException {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(serviceId);
        if (existingClient.isEmpty()) {
            throw new CannotFindClientException(CannotFindClientException.Reason.NO_ID_FOUND);
        } else if (existingSalonService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        Client client = existingClient.get();
        SalonService salonService = existingSalonService.get();
        Optional<Registration> existingRegistration = registrationRepository
                .findAllByClientAndSalonService(client, salonService);
        if (existingRegistration.isEmpty()) {
            throw new CannotFindRegistrationException();
        }
        Registration registration = existingRegistration.get();
        registrationRepository.delete(registration);
        salonServiceRepository.delete(salonService);
        return "The registration and the service were both removed.";
    }

    /**
     * Method for finding client's id by service id.
     *
     * @param serviceId of the service
     * @return client's id
     */
    @Transactional
    public Long getClientOfRegisteredService(Long serviceId) throws CannotFindServiceException {
        Optional<SalonService> existingService = salonServiceRepository.findById(serviceId);
        if (existingService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        SalonService service = existingService.get();
        Optional<Registration> existingRegistration = registrationRepository.findAllBySalonService(service);
        return existingRegistration.map(registration -> registration.getClient().getClientId()).orElse(null);
    }
}
