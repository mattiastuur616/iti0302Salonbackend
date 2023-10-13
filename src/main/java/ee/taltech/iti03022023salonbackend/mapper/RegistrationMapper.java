package ee.taltech.iti03022023salonbackend.mapper;

import ee.taltech.iti03022023salonbackend.dto.RegistrationDto;
import ee.taltech.iti03022023salonbackend.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    @Mapping(target = "registrationId", source = "registration.registrationId")
    @Mapping(target = "serviceName", source = "registration.salonService.serviceName")
    @Mapping(target = "servicePrice", source = "registration.salonService.price")
    @Mapping(target = "startingTime", source = "registration.salonService.startingTime")
    @Mapping(target = "clientFirstName", source = "registration.client.firstName")
    @Mapping(target = "clientLastName", source = "registration.client.lastName")
    @Mapping(target = "cosmeticFirstName", source = "registration.salonService.cosmetic.firstName")
    @Mapping(target = "cosmeticLastName", source = "registration.salonService.cosmetic.lastName")
    @Mapping(target = "registrationDate", source = "registration.registrationDate")
    RegistrationDto registrationToRegistrationDto(Registration registration);
}
