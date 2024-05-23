package com.cfex.contract.contractmanagement.lib.crypto;

import com.cfex.contract.contractmanagement.config.ApplicationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.BouncyCastleAesGcmBytesEncryptor;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class CryptoHelper {
    private final BouncyCastleAesGcmBytesEncryptor encryptor;
    private final Base64.Encoder base64Encoder = Base64.getEncoder();
    private final Base64.Decoder base64Decoder = Base64.getDecoder();
    private final ObjectMapper objectMapper;

    public CryptoHelper(ApplicationProperties config, @Qualifier("springSecurity") ObjectMapper objectMapper) {
        var saltBytes = base64Decoder.decode(config.getCryptoSalt());
        var saltHex = new String(Hex.encode(saltBytes));
        this.encryptor = new BouncyCastleAesGcmBytesEncryptor(config.getCryptoPassword(), saltHex);
        this.objectMapper = objectMapper;
    }

    public String encryptToBase64String(OAuth2AuthorizationRequest request) {
        try {
            return base64Encoder.encodeToString(encryptor.encrypt(objectMapper.writeValueAsBytes(request)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public OAuth2AuthorizationRequest decryptFromBase64String(String data) {
        try {
            return objectMapper.readValue(encryptor.decrypt(base64Decoder.decode(data)), OAuth2AuthorizationRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}