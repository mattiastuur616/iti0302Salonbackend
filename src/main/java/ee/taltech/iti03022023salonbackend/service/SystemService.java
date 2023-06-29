package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SystemService {
    private final ClientRepository clientRepository;
    private final CosmeticRepository cosmeticRepository;
    private final RegistrationRepository registrationRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceStatusRepository serviceStatusRepository;
    private final ServiceTypeRepository serviceTypeRepository;
}
