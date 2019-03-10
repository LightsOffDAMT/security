package ru.lightsoff.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.lightsoff.security.DTO.GenerateTokenRequest;
import ru.lightsoff.security.services.ISecurityService;
import ru.lightsoff.security.services.SecurityService;
import ru.lightsoff.security.wrappers.TokenValidationVerdict;
import ru.lightsoff.security.wrappers.TokenValidationWrapper;

@RestController
public class TokenController {
    @Autowired
    ISecurityService securityService;

    private final Logger log = LoggerFactory.getLogger(SecurityService.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAuthException(Exception e) {
        log.debug("Exception occured" + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/validateToken")
    ResponseEntity<String> validateToken(@RequestBody String token) {
        TokenValidationWrapper wrapper = securityService.validateToken(token);
        if (wrapper.getVerdict() == TokenValidationVerdict.CORRUPTED || wrapper.getVerdict() == TokenValidationVerdict.EXPIRED) {
            return new ResponseEntity<>(wrapper.getMessage(), HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(wrapper.getMessage(), HttpStatus.OK);
    }

    @PostMapping("/generateToken")
    String generateToken(@RequestBody GenerateTokenRequest req){
        return securityService.generateToken(req);
    }

}

