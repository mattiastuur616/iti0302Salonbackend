package ee.taltech.iti03022023salonbackend.mapper.cosmetic;

import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticUserDto;
import ee.taltech.iti03022023salonbackend.model.cosmetic.CosmeticUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CosmeticUserMapper {
    @Mapping(target = "userId", source = "cosmeticUser.userId")
    @Mapping(target = "cosmeticId", source = "cosmeticUser.cosmetic.cosmeticId")
    @Mapping(target = "password", source = "cosmeticUser.password")
    CosmeticUserDto cosmeticUserToCosmeticUserDto(CosmeticUser cosmeticUser);
}
