package com.mv.sulworkcafe.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info().title("Sulwork Café API").version("v1"));
    }

    @Bean
    public GroupedOpenApi v1() {
        return GroupedOpenApi.builder()
            .group("v1")
            .packagesToScan("com.mv.sulworkcafe.controller")
            .pathsToMatch("/api/**")
            .build();
    }
}
