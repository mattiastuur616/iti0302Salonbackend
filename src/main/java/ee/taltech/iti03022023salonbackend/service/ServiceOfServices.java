package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.exception.CannotFindCosmeticException;
import ee.taltech.iti03022023salonbackend.exception.CannotFindServiceException;
import ee.taltech.iti03022023salonbackend.exception.CannotFindStatusException;
import ee.taltech.iti03022023salonbackend.exception.ServiceErrorException;
import ee.taltech.iti03022023salonbackend.mapper.ServiceMapper;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import ee.taltech.iti03022023salonbackend.model.service.ServiceStatus;
import ee.taltech.iti03022023salonbackend.model.service.ServiceType;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.service.SalonServiceRepository;
import ee.taltech.iti03022023salonbackend.repository.service.ServiceStatusRepository;
import ee.taltech.iti03022023salonbackend.repository.service.ServiceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceOfServices {
    @Autowired
    private final CosmeticRepository cosmeticRepository;
    @Autowired
    private final SalonServiceRepository salonServiceRepository;
    @Autowired
    private final ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private final ServiceStatusRepository serviceStatusRepository;
    @Autowired
    private ServiceMapper serviceMapper;

    /**
     * Method for showing all the services existing in the database.
     *
     * @return the list of services
     */
    @Transactional
    public List<SalonServiceDto> getAllSalonServices() {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        for (SalonService salonService : salonServiceRepository.findAll()) {
            salonServiceDtoList.add(serviceMapper.serviceToServiceDto(salonService));
        }
        return salonServiceDtoList;
    }

    /**
     * Method for getting all available services the users can register.
     *
     * @return the list of available services
     */
    @Transactional
    public List<SalonServiceDto> getAvailableSalonServices() {
        List<SalonServiceDto> availableServices = new ArrayList<>();
        for (SalonServiceDto serviceDto : getAllSalonServices()) {
            if (serviceDto.getStatusId() == 1) {
                availableServices.add(serviceDto);
            }
        }
        return availableServices;
    }

    /**
     * Method for getting data of one service.
     *
     * @param id of the service
     * @return dto of the service
     */
    @Transactional
    public SalonServiceDto getSalonServiceById(Long id) throws CannotFindServiceException {
        Optional<SalonService> existingService = salonServiceRepository.findById(id);
        if (existingService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        return serviceMapper.serviceToServiceDto(existingService.get());
    }

    /**
     * Method for adding a new service to the database.
     *
     * @param salonService to be added
     * @return the string explaining the result
     */
    @Transactional
    public String addSalonService(SalonService salonService, Long cosmeticId) throws CannotFindStatusException,
            ServiceErrorException, CannotFindCosmeticException {
        Optional<SalonService> existingSalonService = salonServiceRepository
                .getSalonServicesByCosmeticAndAndStartingTime(salonService.getCosmetic(), salonService.getStartingTime());
        Optional<ServiceType> existingServiceType = serviceTypeRepository
                .findById(Long.parseLong("1"));
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(cosmeticId);
        Optional<ServiceStatus> existingStatus = serviceStatusRepository.findById(Long.parseLong("1"));
        if (existingSalonService.isPresent()) {
            throw new ServiceErrorException(ServiceErrorException.Reason.SERVICE_EXISTS);
        } else if (existingServiceType.isEmpty()) {
            throw new ServiceErrorException(ServiceErrorException.Reason.WRONG_TYPE);
        } else if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_ID_FOUND);
        } else if (existingStatus.isEmpty()) {
            throw new CannotFindStatusException();
        }
        salonService.setCosmetic(existingCosmetic.get());
        salonService.setServiceType(existingServiceType.get());
        salonService.setServiceStatus(existingStatus.get());
        if (salonService.getServiceStatus().getStatusId() != 1) {
            throw new ServiceErrorException(ServiceErrorException.Reason.WRONG_STATUS);
        } else if (salonService.getServiceName().isEmpty()) {
            return "1";
        } else if (salonService.getPrice() == null) {
            return "2";
        } else if (salonService.getDuration() == null) {
            return "3";
        } else if (salonService.getStartingTime() == null) {
            return "4";
        }
        salonServiceRepository.save(salonService);
        return "0";
    }

    /**
     * Method for removing the service from the database.
     *
     * @param id of the service to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeSalonService(Long id) throws CannotFindServiceException {
        Optional<SalonService> existingSalonService = salonServiceRepository.findById(id);
        if (existingSalonService.isEmpty()) {
            throw new CannotFindServiceException();
        }
        SalonService salonService = existingSalonService.get();
        salonServiceRepository.delete(salonService);
        return "The service with the id " + salonService.getServiceId() + " was removed from the database.";
    }
}
