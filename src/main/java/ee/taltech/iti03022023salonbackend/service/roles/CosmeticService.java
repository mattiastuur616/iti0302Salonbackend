package ee.taltech.iti03022023salonbackend.service.roles;

import ee.taltech.iti03022023salonbackend.config.ValidityCheck;
import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticDto;
import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticUserDto;
import ee.taltech.iti03022023salonbackend.exception.CannotFindCosmeticException;
import ee.taltech.iti03022023salonbackend.exception.UserMissingException;
import ee.taltech.iti03022023salonbackend.mapper.ServiceMapper;
import ee.taltech.iti03022023salonbackend.mapper.cosmetic.CosmeticMapper;
import ee.taltech.iti03022023salonbackend.mapper.cosmetic.CosmeticUserMapper;
import ee.taltech.iti03022023salonbackend.model.admin.AdminUser;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.model.cosmetic.CosmeticUser;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import ee.taltech.iti03022023salonbackend.repository.admin.AdminUserRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientUserRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticUserRepository;
import ee.taltech.iti03022023salonbackend.repository.service.SalonServiceRepository;
import ee.taltech.iti03022023salonbackend.service.ServiceOfServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CosmeticService {
    private final ValidityCheck validityCheck;
    @Autowired
    private final CosmeticRepository cosmeticRepository;
    @Autowired
    private final CosmeticUserRepository cosmeticUserRepository;
    @Autowired
    private final ClientUserRepository clientUserRepository;
    @Autowired
    private final AdminUserRepository adminUserRepository;
    @Autowired
    private final SalonServiceRepository salonServiceRepository;
    @Autowired
    private final ServiceOfServices serviceOfServices;
    @Autowired
    private CosmeticMapper cosmeticMapper;
    @Autowired
    private CosmeticUserMapper cosmeticUserMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for showing all the cosmetics existing in the database.
     *
     * @return the list of cosmetics
     */
    @Transactional
    public List<CosmeticDto> getAllCosmetics() {
        List<CosmeticDto> cosmeticDtoList = new ArrayList<>();
        for (Cosmetic cosmetic : cosmeticRepository.findAll()) {
            cosmeticDtoList.add(cosmeticMapper.cosmeticToCosmeticDto(cosmetic));
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
    public CosmeticDto getCosmetic(Long id) throws CannotFindCosmeticException {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(id);
        if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_ID_FOUND);
        }
        return cosmeticMapper.cosmeticToCosmeticDto(existingCosmetic.get());
    }

    /**
     * Method for receiving cosmetic's id.
     *
     * @param email of the cosmetic
     * @return cosmetic's id
     */
    @Transactional
    public Long getCosmeticId(String email) throws CannotFindCosmeticException {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findCosmeticsByEmailIgnoreCase(email);
        if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_EMAIL_FOUND);
        }
        Cosmetic actualCosmetic = existingCosmetic.get();
        return actualCosmetic.getCosmeticId();
    }

    /**
     * Method for showing all the users owned by the employed cosmetics.
     *
     * @return the list of users
     */
    @Transactional
    public List<CosmeticUserDto> getAllCosmeticUsers() {
        List<CosmeticUserDto> cosmeticUserDtoList = new ArrayList<>();
        for (CosmeticUser cosmeticUser : cosmeticUserRepository.findAll()) {
            cosmeticUserDtoList.add(cosmeticUserMapper.cosmeticUserToCosmeticUserDto(cosmeticUser));
        }
        return cosmeticUserDtoList;
    }

    /**
     * Method for adding a new cosmetic into the database.
     * Because of each one of the cosmetic needs to own unique email address the function check
     * if there isn't already existing cosmetic with that email.
     *
     * <p>
     * Returns the status number which explain the result:
     * 0 - correct and a new user is registered
     * 1 - cosmetic already exists in the database
     * 2 - password already in use
     * 3 - unacceptable password
     * 4 - unacceptable phone number
     * 5 - unacceptable id code
     *
     * @param cosmetic to be added
     * @param password of the user
     * @return status number
     */
    @Transactional
    public String addCosmetic(Cosmetic cosmetic, String password) {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findCosmeticsByEmailIgnoreCase(cosmetic.getEmail());
        if (existingCosmetic.isPresent()) {
            return "1";
        } else if (passwordExists(password)) {
            return "2";
        } else if (!validityCheck.isValidPassword(password)) {
            return "3";
        } else if (!validityCheck.isValidPhoneNumber(cosmetic.getPhoneNumber())) {
            return "4";
        } else if (!validityCheck.isValidIdCode(cosmetic.getIdCode())) {
            return "5";
        }
        cosmeticRepository.save(cosmetic);
        CosmeticUser newCosmeticUser = new CosmeticUser();
        newCosmeticUser.setPassword(passwordEncoder.encode(password));
        newCosmeticUser.setCosmetic(cosmetic);
        cosmeticUserRepository.save(newCosmeticUser);
        return "0";
    }

    /**
     * Function to check if entered password doesn't already exist.
     *
     * @param password entered by the new user
     * @return boolean
     */
    public boolean passwordExists(String password) {
        List<String> passwords = new ArrayList<>();
        for (CosmeticUser user : cosmeticUserRepository.findAll()) {
            passwords.add(user.getPassword());
        }
        for (ClientUser clientUser : clientUserRepository.findAll()) {
            passwords.add(clientUser.getPassword());
        }
        for (AdminUser adminUser : adminUserRepository.findAll()) {
            passwords.add(adminUser.getPassword());
        }
        for (String pass : passwords) {
            if (passwordEncoder.matches(password, pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method used when logging in.
     * It checks whether the user has put correct credentials.
     *
     * @param email of the user
     * @param password of the user
     * @return boolean
     */
    @Transactional
    public boolean isValidCosmetic(String email, String password) {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findCosmeticsByEmailIgnoreCase(email);
        if (existingCosmetic.isEmpty()) {
            return false;
        }
        Cosmetic cosmetic = existingCosmetic.get();
        Optional<CosmeticUser> existingUser = cosmeticUserRepository.findCosmeticUsersByCosmetic(cosmetic);
        if (existingUser.isEmpty()) {
            return false;
        }
        CosmeticUser cosmeticUser = existingUser.get();
        return passwordEncoder.matches(password, cosmeticUser.getPassword());
    }

    /**
     * Method for getting cosmetic's name
     *
     * @param email of cosmetic
     * @return string of cosmetic's name
     */
    @Transactional
    public String getCosmeticName(String email) throws CannotFindCosmeticException {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findCosmeticsByEmailIgnoreCase(email);
        if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_EMAIL_FOUND);
        }
        Cosmetic actualCosmetic = existingCosmetic.get();
        return actualCosmetic.getFirstName() + " " + actualCosmetic.getLastName();
    }

    /**
     * Method for removing the cosmetic from the database.
     * The function checks if the cosmetic even exists before trying to remove them.
     *
     * @param id of the cosmetic to be removed
     * @return the string explaining the result
     */
    @Transactional
    public String removeCosmetic(Long id) throws CannotFindCosmeticException, UserMissingException {
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(id);
        if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_ID_FOUND);
        }
        Cosmetic cosmetic = existingCosmetic.get();
        Optional<CosmeticUser> existingUser = cosmeticUserRepository.findCosmeticUsersByCosmetic(cosmetic);
        if (existingUser.isEmpty()) {
            throw new UserMissingException(UserMissingException.Role.COSMETIC);
        }
        CosmeticUser cosmeticUser = existingUser.get();
        cosmeticUserRepository.delete(cosmeticUser);
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
    public List<SalonServiceDto> getAllServicesOfCosmetic(Long id) throws CannotFindCosmeticException {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        Optional<Cosmetic> existingCosmetic = cosmeticRepository.findById(id);
        if (existingCosmetic.isEmpty()) {
            throw new CannotFindCosmeticException(CannotFindCosmeticException.Reason.NO_ID_FOUND);
        }
        for (SalonService salonService : salonServiceRepository.findAllByCosmetic(existingCosmetic.get())) {
            salonServiceDtoList.add(serviceMapper.serviceToServiceDto(salonService));
        }
        return salonServiceDtoList;
    }

    /**
     * Method for showing all cosmetic's services which are open for register.
     *
     * @param id of the cosmetic
     * @return the list of service dto-s
     */
    @Transactional
    public List<SalonServiceDto> getOpenServices(Long id) throws CannotFindCosmeticException {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        for (SalonServiceDto salonServiceDto : getAllServicesOfCosmetic(id)) {
            if (salonServiceDto.getStatusId() == 1) {
                salonServiceDtoList.add(salonServiceDto);
            }
        }
        return salonServiceDtoList;
    }

    /**
     * Method for showing all cosmetic's services which are registered by clients.
     *
     * @param id of the cosmetic
     * @return the list of service dto-s
     */
    @Transactional
    public List<SalonServiceDto> getRegisteredServices(Long id) throws CannotFindCosmeticException {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        for (SalonServiceDto salonServiceDto : getAllServicesOfCosmetic(id)) {
            if (salonServiceDto.getStatusId() == 2) {
                salonServiceDtoList.add(salonServiceDto);
            }
        }
        return salonServiceDtoList;
    }

    /**
     * Method for showing all cosmetic's services which are open for registration.
     *
     * @param id of the cosmetic
     * @return the list of service dto-s
     */
    @Transactional
    public List<SalonServiceDto> getFinishedServices(Long id) throws CannotFindCosmeticException {
        List<SalonServiceDto> salonServiceDtoList = new ArrayList<>();
        for (SalonServiceDto salonServiceDto : getAllServicesOfCosmetic(id)) {
            if (salonServiceDto.getStatusId() == 3) {
                salonServiceDtoList.add(salonServiceDto);
            }
        }
        return salonServiceDtoList;
    }
}
