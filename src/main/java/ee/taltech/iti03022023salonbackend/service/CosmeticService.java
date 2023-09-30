package ee.taltech.iti03022023salonbackend.service;

import ee.taltech.iti03022023salonbackend.dto.CosmeticDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.model.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.SalonService;
import ee.taltech.iti03022023salonbackend.repository.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.SalonServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CosmeticService {
    private final CosmeticRepository cosmeticRepository;
    private final SalonServiceRepository salonServiceRepository;
    private final ServiceOfServices serviceOfServices;


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
     * Method for getting one cosmetic by id.
     *
     * @param id of the cosmetic
     * @return cosmetic dto
     */
    @Transactional
    public CosmeticDto getCosmetic(Long id) {
        Optional<Cosmetic> cosmetic = cosmeticRepository.findById(id);
        return cosmetic.map(this::convertIntoCosmeticDto).orElse(null);
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
            salonServiceDtoList.add(serviceOfServices.convertIntoSalonServiceDto(salonService));
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
}
