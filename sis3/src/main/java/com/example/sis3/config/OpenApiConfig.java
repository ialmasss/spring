package com.example.sis3.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order & Event-Driven API (SIS 3)")
                        .description("REST API with OpenAPI documentation, resilient RestClient, AOP logging/caching, and Apache Kafka with DLT")
                        .version("v1.0.0")
                        .contact(new Contact().name("Backend Engineering Team")));
    }
}