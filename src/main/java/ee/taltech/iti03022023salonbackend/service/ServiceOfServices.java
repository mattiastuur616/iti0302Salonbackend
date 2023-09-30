package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.SalonService;
import ee.taltech.iti03022023salonbackend.model.ServiceType;
import ee.taltech.iti03022023salonbackend.repository.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.SalonServiceRepository;
import ee.taltech.iti03022023salonbackend.repository.ServiceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceOfServices {
    private final CosmeticRepository cosmeticRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceTypeRepository serviceTypeRepository;

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
    public SalonServiceDto getSalonServiceById(Long id) {
        Optional<SalonService> service = salonServiceRepository.findById(id);
        return service.map(this::convertIntoSalonServiceDto).orElse(null);
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
}
