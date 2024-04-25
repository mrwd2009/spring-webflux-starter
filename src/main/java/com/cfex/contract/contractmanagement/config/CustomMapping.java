package com.cfex.contract.contractmanagement.config;

import com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator.CustomHandler;
import com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class CustomMapping {
    @Autowired
    public void setHandlerMapping(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping, UserHandler userHandler) throws NoSuchMethodException {
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("user/{userId}").build();
        Method method = UserHandler.class.getMethod("getUser", Long.class);
        handlerMapping.registerMapping(requestMappingInfo, userHandler, method);
    }

    @Bean
    public RouterFunction<ServerResponse> route(CustomHandler handler) {
        return RouterFunctions.route()
                .GET("/hello", handler::hello)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> route2(CustomHandler handler) {
        return RouterFunctions.route()
                .GET("/hello2", handler::hello)
                .build();
    }
}
