package ee.taltech.iti03022023salonbackend.mapper.admin;

import ee.taltech.iti03022023salonbackend.dto.admin.AdminDto;
import ee.taltech.iti03022023salonbackend.model.admin.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDto adminToAdminDto(Admin admin);
}
