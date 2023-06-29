package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.dto.ClientDto;
import ee.taltech.iti03022023salonbackend.model.Client;
import ee.taltech.iti03022023salonbackend.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SystemController {
    private final SystemService systemService;

    @GetMapping("/allClients")
    public List<ClientDto> getAllClients() {
        return systemService.getAllClients();
    }

    @PostMapping("/addClient")
    public String addClient(@RequestBody Client client) {
        return systemService.addClient(client);
    }

    @DeleteMapping("/removeClient/{id}")
    public String removeClient(@PathVariable Long id) {
        return systemService.removeClient(id);
    }
}
