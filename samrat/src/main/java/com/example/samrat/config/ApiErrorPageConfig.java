package com.example.samrat.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ApiErrorPageConfig {

    @Bean
    public ErrorPageRegistrar apiErrorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/api-error/401"));
                registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/api-error/403"));
            }
        };
    }
}
