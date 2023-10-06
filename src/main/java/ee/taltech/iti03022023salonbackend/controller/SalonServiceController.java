package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import ee.taltech.iti03022023salonbackend.service.ServiceOfServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class SalonServiceController {
    private final ServiceOfServices serviceOfServices;


    /**
     * Get request for showing all the services in the salon.
     *
     * @return the list of services
     */
    @GetMapping("/allServices")
    public List<SalonServiceDto> getAllSalonServices() {
        return serviceOfServices.getAllSalonServices();
    }

    /**
     * Get request for showing all available services.
     *
     * @return the list of available services
     */
    @GetMapping("/availableServices")
    public List<SalonServiceDto> getAvailableSalonServices() {
        return serviceOfServices.getAvailableSalonServices();
    }

    /**
     * Get request for getting data about one specific service.
     *
     * @param id of the service
     * @return dto of the service
     */
    @GetMapping("/service/{id}")
    public SalonServiceDto getSalonServiceById(@PathVariable Long id) {
        return serviceOfServices.getSalonServiceById(id);
    }

    /**
     * Post request for adding a new service to the salon.
     *
     * @param salonService to be added
     * @return the string explaining the result
     */
    @PostMapping("/addService")
    public String addSalonService(@RequestBody SalonService salonService) {
        return serviceOfServices.addSalonService(salonService);
    }

    /**
     * Delete request for removing the service from the salon.
     *
     * @param id of the service to be removed
     * @return the string explaining the result
     */
    @DeleteMapping("/removeService/{id}")
    public String removeSalonService(@PathVariable Long id) {
        return serviceOfServices.removeSalonService(id);
    }
}
