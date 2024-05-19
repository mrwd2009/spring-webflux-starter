package com.cfex.contract.contractmanagement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationProperties {
    @Value("${cfex-oc.env:development}")
    private String env;

    public Boolean isDev() {
        return this.env.contains("development") || this.env.contains("test");
    }

    @Value("${cfex-oc.crypto.password}")
    private String cryptoPassword;

    @Value("${cfex-oc.crypto.salt}")
    private String cryptoSalt;

    @Value("${cfex-oc.auth.request-cookie-key:cfex-oc-authorization-request}")
    private String authorizationRequestCookieKey;

    @Value("${cfex-oc.auth.request-cookie-age:300}")
    private Long authorizationRequestCookieAge;

    @Value("${cfex-oc.jwt.rsa-private-key}")
    private String jwtRsaPrivateKey;

    @Value("${cfex-oc.jwt.rsa-public-key}")
    private String jwtRsaPublicKey;

    @Value("${cfex-oc.jwt.rsa-key-id}")
    private String jwtRsaKeyId;

    @Value("${cfex-oc.session.cookie-key}")
    private String sessionCookieKey;

    @Value("${cfex-oc.session.effective-minutes}")
    private Integer sessionEffectiveMinutes;
}
