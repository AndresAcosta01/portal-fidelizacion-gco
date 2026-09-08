package com.gco.fidelizacion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Portal de Fidelización GCO API",
        version = "1.0.0",
        description = "API REST para el registro de clientes y la gestión de catálogos del Portal de Fidelización GCO.",
        contact = @Contact(
            name = "Andres Acosta"
        )
    )
)
public class OpenApiConfig {
}