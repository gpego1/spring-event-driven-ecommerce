package com.ecommerce.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI.
 * Disponível em: http://localhost:8080/swagger-ui
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .description("Backend de e-commerce — Event-Driven Architecture (preparado para Kafka)")
                        .version("1.0.0")
                        .contact(new Contact().name("Portfolio").email("dev@portfolio.com")));
    }
}
