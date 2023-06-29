package ee.taltech.iti03022023salonbackend.controller;

import ee.taltech.iti03022023salonbackend.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SystemController {
    private final SystemService systemService;
}
