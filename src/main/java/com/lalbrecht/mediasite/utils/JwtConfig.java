package com.lalbrecht.mediasite.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Component
public class JwtConfig {

    private final SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;

    private final Key signingKey;

    public JwtConfig() {
        String salt = "lakjuhewbalfaiuhlilawsekjhbewliufbalsdkjewlhiaulhwekjawhbaklwjusygo";
        byte[] saltyBytes = DatatypeConverter.parseBase64Binary(salt);
        signingKey = new SecretKeySpec(saltyBytes, sigAlg.getJcaName());
    }

    public int getExpiration() {
        return 60 * 60 * 1000;
    }

    public SignatureAlgorithm getSigAlg() {
        return sigAlg;
    }

    public Key getSigningKey() {
        return signingKey;
    }
}
