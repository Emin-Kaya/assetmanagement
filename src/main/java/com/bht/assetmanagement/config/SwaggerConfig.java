package com.bht.assetmanagement.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Asset Management")
                        .version("0.0.1")
                        .description("Documentation for Asset Management API")
                        .contact(new Contact()
                                .email("s87199@bht-berlin.de")
                                .name("Emin Kaya")
                                .url("https://www.bht-berlin.de")));

    }
}
