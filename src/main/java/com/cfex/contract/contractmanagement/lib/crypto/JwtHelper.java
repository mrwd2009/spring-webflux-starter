package com.cfex.contract.contractmanagement.lib.crypto;

import com.cfex.contract.contractmanagement.config.ApplicationProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtHelper {
    public static String AUTH0_CLAIM_ROLES = "cfex/roles";
    public static String AUTH0_CLAIM_INVITATION = "cfex/invitation";
    public static String AUTH0_CLAIM_MARKETPLACE_CODE = "cfex/marketplaceCode";
    public static String AUTH0_CLAIM_SUBSCRIPTIONS = "cfex/subscriptions";
    public static String AUTH0_CLAIM_ORG_ID = "org_id";
    public static String OC_CLAIM_EMAIL = "oc/email";
    public static String OC_CLAIM_NONCE = "oc/nonce";
    public static String OC_CLAIM_ROLES = "oc/roles";
    public static String OC_CLAIM_SUBSCRIPTIONS = "oc/subscriptions";
    public static String OC_CLAIM_ORG_CODE = "oc/marketplaceCode";
    public static String OC_CLAIM_SESSION_ISSUED_AT = "oc/sessionIssuedAt";
    public static String OC_CLAIM_CLIENT_REGISTRATION_ID = "oc/clientRegistrationId";

    private final Base64.Decoder base64Decoder = Base64.getDecoder();
    private final NimbusJwtEncoder jwtEncoder;
    private final NimbusReactiveJwtDecoder reactiveJwtDecoder;

    public JwtHelper(ApplicationProperties properties) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var publicKeyBytes = base64Decoder.decode(properties.getJwtRsaPublicKey());
        var privateKeyBytes = base64Decoder.decode(properties.getJwtRsaPrivateKey());

        var publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        var privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        var keyFactory = KeyFactory.getInstance("RSA");

        var publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        var privateKey =(RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        var rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(properties.getJwtRsaKeyId())
                .build();
        var jwkSet = new JWKSet(rsaKey);

        jwtEncoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(jwkSet));
        reactiveJwtDecoder = NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
    }

    public Mono<Jwt> encode(JwtClaimsSet claims) {
        return Mono.just(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)));
    }

    public Mono<Jwt> decode(String token) {
        return reactiveJwtDecoder.decode(token);
    }
}
