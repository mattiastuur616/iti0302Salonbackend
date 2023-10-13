package ee.taltech.iti03022023salonbackend.mapper.client;

import ee.taltech.iti03022023salonbackend.dto.client.ClientUserDto;
import ee.taltech.iti03022023salonbackend.model.client.ClientUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientUserMapper {
    @Mapping(target = "userId", source = "clientUser.userId")
    @Mapping(target = "clientId", source = "clientUser.client.clientId")
    @Mapping(target = "password", source = "clientUser.password")
    public ClientUserDto clientUserToClientUserDto(ClientUser clientUser);
}
