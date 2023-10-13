package ee.taltech.iti03022023salonbackend.mapper.admin;

import ee.taltech.iti03022023salonbackend.dto.admin.AdminUserDto;
import ee.taltech.iti03022023salonbackend.model.admin.AdminUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {
    @Mapping(target = "userId", source = "adminUser.user_id")
    @Mapping(target = "adminId", source = "adminUser.admin.adminId")
    @Mapping(target = "password", source = "adminUser.password")
    AdminUserDto adminUserToAdminUserDto(AdminUser adminUser);
}
