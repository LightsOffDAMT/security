package ru.lightsoff.security.services;

import ru.lightsoff.security.DTO.GenerateTokenRequest;
import ru.lightsoff.security.wrappers.TokenValidationWrapper;

public interface ISecurityService {
    TokenValidationWrapper validateToken(String token);
    String generateToken(GenerateTokenRequest req);

}
