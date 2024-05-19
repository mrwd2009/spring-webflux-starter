package com.cfex.contract.contractmanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class ApplicationConfiguration {
    @Bean
    ObjectMapper getObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.getClass().getClassLoader()));
        return mapper;
    }

}
