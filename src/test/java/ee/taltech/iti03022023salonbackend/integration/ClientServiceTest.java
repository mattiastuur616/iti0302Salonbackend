package ee.taltech.iti03022023salonbackend.integration;

import ee.taltech.iti03022023salonbackend.Iti0302SalonbackendApplication;
import ee.taltech.iti03022023salonbackend.controller.RegistrationController;
import ee.taltech.iti03022023salonbackend.controller.SalonServiceController;
import ee.taltech.iti03022023salonbackend.controller.roles.AdminController;
import ee.taltech.iti03022023salonbackend.controller.roles.ClientController;
import ee.taltech.iti03022023salonbackend.controller.roles.CosmeticController;
import ee.taltech.iti03022023salonbackend.exception.CannotFindClientException;
import ee.taltech.iti03022023salonbackend.model.client.Client;
import ee.taltech.iti03022023salonbackend.model.cosmetic.Cosmetic;
import ee.taltech.iti03022023salonbackend.repository.RegistrationRepository;
import ee.taltech.iti03022023salonbackend.repository.admin.AdminRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientRepository;
import ee.taltech.iti03022023salonbackend.repository.client.ClientUserRepository;
import ee.taltech.iti03022023salonbackend.repository.cosmetic.CosmeticRepository;
import ee.taltech.iti03022023salonbackend.repository.service.SalonServiceRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Iti0302SalonbackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ClientServiceTest {
    @LocalServerPort
    private int port;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ClientUserRepository clientUserRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SalonServiceRepository salonServiceRepository;
    @Autowired
    private CosmeticRepository cosmeticRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RegistrationController registrationController;
    @Autowired
    private ClientController clientController;
    @Autowired
    private SalonServiceController salonServiceController;
    @Autowired
    private CosmeticController cosmeticController;
    @Autowired
    private AdminController adminController;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }

    @BeforeEach
    public void initEach() {
        registrationRepository.deleteAll();
        clientUserRepository.deleteAll();
        clientRepository.deleteAll();
        Client firstClient = new Client();
        firstClient.setFirstName("Ahsoka");
        firstClient.setLastName("Tano");
        firstClient.setEmail("ahsoka.tano@gmail.com");
        firstClient.setIdCode("60204152839");
        firstClient.setPhoneNumber("75833989");
        firstClient.setHomeAddress("Jumalate tee 45");
        firstClient.setDateOfBirth(new Date(2, 4, 15));
        clientController.addClient(firstClient, "Moraigofly5");

        Client secondClient = new Client();
        secondClient.setFirstName("Anakin");
        secondClient.setLastName("Skywalker");
        secondClient.setEmail("anakin.skywalker@gmail.com");
        secondClient.setIdCode("50402114928");
        secondClient.setPhoneNumber("49228832");
        secondClient.setHomeAddress("Galaktika tee 45");
        secondClient.setDateOfBirth(new Date(3, 2, 12));
        clientController.addClient(secondClient, "R2d2andc3po");

        Cosmetic firstCosmetic = new Cosmetic();
        firstCosmetic.setFirstName("Carmen");
        firstCosmetic.setLastName("Karjane");
        firstCosmetic.setPhoneNumber("14456789");
        firstCosmetic.setEmail("carmen.karjane@gmail.com");
        firstCosmetic.setIdCode("50102159482");
        firstCosmetic.setDateOfBirth(new Date(11, 1, 15));
        firstCosmetic.setHomeAddress("Paju tee 54");
        cosmeticController.addCosmetic(firstCosmetic, "Bestpassword1");

        Cosmetic secondCosmetic = new Cosmetic();
        secondCosmetic.setFirstName("Mari");
        secondCosmetic.setLastName("Pääsuke");
        secondCosmetic.setPhoneNumber("Pääsuke");
        secondCosmetic.setEmail("mari.paasuke@gmail.com");
        secondCosmetic.setIdCode("50208114833");
        secondCosmetic.setDateOfBirth(new Date(12, 1, 24));
        secondCosmetic.setHomeAddress("Kase tee 28");
        cosmeticController.addCosmetic(secondCosmetic, "Bestpassword2");
    }

    @SneakyThrows
    @Test
    public void testAddedClients() {
        HttpEntity<Client> entity = new HttpEntity<Client>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/allClients"),
                HttpMethod.GET, entity, String.class);
        assertTrue(response.getBody().contains("Ahsoka"));
        assertTrue(response.getBody().contains("Anakin"));
        assertEquals(0, clientController
                .getClient(clientController.getClientId("ahsoka.tano@gmail.com")).getMoney());
        assertEquals(0, clientController
                .getClient(clientController.getClientId("anakin.skywalker@gmail.com")).getMoney());
        assertEquals(2, clientRepository.findAll().size());
    }

    @SneakyThrows
    @Test
    public void testAddNewClientSuccess() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("maria.rand@gmail.com");
        newClient.setIdCode("60205278493");
        newClient.setPhoneNumber("37728322");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=Newpassword1"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("0", response.getBody());
        assertEquals("Maria", clientController
                .getClient(clientController.getClientId("maria.rand@gmail.com")).getFirstName());
    }

    @Test
    public void testAddExistingClient() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("anakin.skywalker@gmail.com");
        newClient.setIdCode("60205278493");
        newClient.setPhoneNumber("37728322");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=Newpassword3"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("1", response.getBody());
    }

    @Test
    public void testAddClientWithTakenPassword() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("maria.rand@gmail.com");
        newClient.setIdCode("60205278493");
        newClient.setPhoneNumber("37728322");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=R2d2andc3po"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("2", response.getBody());
    }

    @Test
    public void testAddClientInvalidPassword() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("maria.rand@gmail.com");
        newClient.setIdCode("60205278493");
        newClient.setPhoneNumber("37728322");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=simplepass1"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("3", response.getBody());
    }

    @Test
    public void testAddClientInvalidPhoneNumber() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("maria.rand@gmail.com");
        newClient.setIdCode("60205278493");
        newClient.setPhoneNumber("377282");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=Newpassword1"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("4", response.getBody());
    }

    @Test
    public void testAddClientInvalidIdCode() {
        Client newClient = new Client();
        newClient.setFirstName("Maria");
        newClient.setLastName("Rand");
        newClient.setEmail("maria.rand@gmail.com");
        newClient.setIdCode("60215278493");
        newClient.setPhoneNumber("37728322");
        newClient.setHomeAddress("Kannu tee 66");
        newClient.setDateOfBirth(new Date(220, 11, 4));

        HttpEntity<Client> entity = new HttpEntity<Client>(newClient, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/addClient?password=Newpassword1"),
                HttpMethod.POST, entity, String.class);

        assertEquals("200 OK", response.getStatusCode().toString());
        assertEquals("5", response.getBody());
    }

    @SneakyThrows
    @Test
    public void testAddMoneyToClient() {
        assertEquals(0, clientController
                .getClient(clientController.getClientId("ahsoka.tano@gmail.com")).getMoney());
        clientController.addMoney(clientController.getClientId("ahsoka.tano@gmail.com"), 50);
        assertEquals(50, clientController
                .getClient(clientController.getClientId("ahsoka.tano@gmail.com")).getMoney());
        clientController.addMoney(clientController.getClientId("ahsoka.tano@gmail.com"), 7);
        assertEquals(57, clientController
                .getClient(clientController.getClientId("ahsoka.tano@gmail.com")).getMoney());
    }

    @SneakyThrows
    @Test
    public void removeClientSuccess() {
        clientController.removeClient(clientController.getClientId("ahsoka.tano@gmail.com"));
        assertEquals(1, clientController.getAllClients().size());
        assertThrows(CannotFindClientException.class, () -> clientController.getClient(clientController.getClientId("ahsoka.tano@gmail.com")));
    }
}
