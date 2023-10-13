package ee.taltech.iti03022023salonbackend.mapper.client;

import ee.taltech.iti03022023salonbackend.dto.client.ClientDto;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto clientToClientDto(Client client);

    Client clientDtoToClient(ClientDto clientDto);
}
