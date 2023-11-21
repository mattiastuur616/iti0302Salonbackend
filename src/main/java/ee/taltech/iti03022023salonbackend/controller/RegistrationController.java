package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.dto.RegistrationDto;
import ee.taltech.iti03022023salonbackend.exception.*;
import ee.taltech.iti03022023salonbackend.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@RestController
public class RegistrationController {
    @Autowired
    private final RegistrationService registrationService;


    /**
     * Get request for showing all registrations.
     *
     * @return the list of registrations
     */
    @GetMapping("/allRegistrations")
    public List<RegistrationDto> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    /**
     * Post request for registering a service for the client.
     *
     * @param clientId of the client who registers the service
     * @param serviceId of the service to be registered
     * @return the string explaining the result
     */
    @PostMapping("/registerService")
    public String registerService(@RequestParam Long clientId, @RequestParam Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindStatusException {
        return registrationService.registerService(clientId, serviceId);
    }

    /**
     * Delete request for canceling the registration.
     *
     * @param clientId of the client who cancels the service
     * @param serviceId of the service to be canceled
     * @return the string explaining the result
     */
    @DeleteMapping("/cancelService")
    public String cancelService(@RequestParam Long clientId, @RequestParam Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindRegistrationException, CannotFindStatusException {
        return registrationService.cancelService(clientId, serviceId);
    }

    /**
     * Put request for finishing the registration.
     *
     * @param clientId of the client whose registration will be finished
     * @param serviceId of the service to be finished
     * @return the string explaining the result
     */
    @PutMapping("/finishService")
    public String finishService(@RequestParam Long clientId, @RequestParam Long serviceId) throws CannotFindClientException,
            ServiceActionException, CannotFindServiceException, CannotFindRegistrationException, CannotFindStatusException {
        return registrationService.finishService(clientId, serviceId);
    }

    /**
     * Post request for removing both registration and a service.
     *
     * @param clientId of the client
     * @param serviceId of the service
     * @return string
     */
    @DeleteMapping("/removeRegistration")
    public String removeServiceAndRegistration(@RequestParam Long clientId, @RequestParam Long serviceId) throws CannotFindClientException,
            CannotFindServiceException, CannotFindRegistrationException {
        return registrationService.removeServiceAndRegistration(clientId, serviceId);
    }

    /**
     * Get request for client id who has registered the service.
     *
     * @param serviceId of the service
     * @return client's id
     */
    @GetMapping("/registeredClient/{serviceId}")
    public Long getClientOfRegisteredService(@PathVariable Long serviceId) throws CannotFindServiceException {
        return registrationService.getClientOfRegisteredService(serviceId);
    }
}
