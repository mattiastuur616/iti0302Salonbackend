package ee.taltech.iti03022023salonbackend.mapper;

import ee.taltech.iti03022023salonbackend.dto.SalonServiceDto;
import ee.taltech.iti03022023salonbackend.model.service.SalonService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "serviceId", source = "service.serviceId")
    @Mapping(target = "name", source = "service.serviceName")
    @Mapping(target = "price", source = "service.price")
    @Mapping(target = "typeId", source = "service.serviceType.typeId")
    @Mapping(target = "duration", source = "service.duration")
    @Mapping(target = "startingTime", source = "service.startingTime")
    @Mapping(target = "statusId", source = "service.serviceStatus.statusId")
    @Mapping(target = "cosmeticId", source = "service.cosmetic.cosmeticId")
    SalonServiceDto serviceToServiceDto(SalonService service);
}
