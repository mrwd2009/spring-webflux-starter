package com.cfex.contract.contractmanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class ApplicationConfiguration {
    @Bean("springSecurity")
    ObjectMapper getSpringSecurityObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.getClass().getClassLoader()));
        return mapper;
    }

    @Bean
    @Primary
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }



}
