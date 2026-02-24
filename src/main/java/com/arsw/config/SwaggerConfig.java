package com.arsw.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI marketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Market Gateway API")
                        .description("API para consultar datos del mercado de valores con múltiples proveedores")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Market Gateway Team")
                                .email("support@marketgateway.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.marketgateway.com")
                                .description("Servidor de producción")
                ));
    }
}
