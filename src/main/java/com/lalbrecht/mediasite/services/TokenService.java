package com.lalbrecht.mediasite.services;

import com.lalbrecht.mediasite.dtos.responses.Principal;
import com.lalbrecht.mediasite.utils.JwtConfig;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class TokenService {
    @Autowired
    private final JwtConfig jwtConfig;

    public String generateToken(Principal p) {
        long now = System.currentTimeMillis();
        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(p.getId())
                .setIssuer("mediaSite")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .setSubject(p.getUsername())
                .claim("mod", p.isMod())
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return tokenBuilder.compact();
    }
}
