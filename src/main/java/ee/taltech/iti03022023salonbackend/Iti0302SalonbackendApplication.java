package ee.taltech.iti03022023salonbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Iti0302SalonbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Iti0302SalonbackendApplication.class, args);
    }

}
