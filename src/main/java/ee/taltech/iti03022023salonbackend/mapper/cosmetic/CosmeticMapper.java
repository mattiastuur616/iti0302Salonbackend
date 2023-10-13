package ee.taltech.iti03022023salonbackend.mapper.cosmetic;

import ee.taltech.iti03022023salonbackend.dto.cosmetic.CosmeticDto;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CosmeticMapper {
    CosmeticDto cosmeticToCosmeticDto(Cosmetic cosmetic);
}
