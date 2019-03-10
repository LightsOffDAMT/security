package ru.lightsoff.security.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.lightsoff.security.DTO.GenerateTokenRequest;
import ru.lightsoff.security.wrappers.TokenValidationVerdict;
import ru.lightsoff.security.wrappers.TokenValidationWrapper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ru.lightsoff.security.messages.TokenValidationMessages.*;

@Service
public class SecurityService implements  ISecurityService{
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    @Value("${secreyKey}")
    String secretKey;

    public TokenValidationWrapper validateToken(String token) {
        LOGGER.debug("Parsing token");
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.debug("{}, specifically: {}", TOKEN_CORRUPTED_MSG, e.getMessage());
            return new TokenValidationWrapper(TokenValidationVerdict.CORRUPTED, TOKEN_CORRUPTED_MSG);
        }
        LOGGER.debug("Checking expiration date");
        if (claims.getExpiration().after(new Date())) {
            LOGGER.debug("Token is valid");
            return new TokenValidationWrapper(TokenValidationVerdict.OK, TOKEN_VALID_MSG);
        } else return new TokenValidationWrapper(TokenValidationVerdict.EXPIRED, TOKEN_EXPIRED_MSG);
    }

    public String generateToken(GenerateTokenRequest user){
        LOGGER.debug("Adding claims to token");
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("id", user.getId());
        tokenData.put("username", user.getUsername());
        tokenData.put("email", user.getEmail());

        JwtBuilder jwtBuilder = Jwts.builder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        LOGGER.debug("Setting expiration date: " + calendar.getTime());
        jwtBuilder.setClaims(tokenData);
        jwtBuilder.setExpiration(calendar.getTime());

        LOGGER.debug("Signing token");
        return jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
}
