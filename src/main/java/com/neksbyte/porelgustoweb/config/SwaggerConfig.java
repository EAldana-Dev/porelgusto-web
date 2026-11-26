package com.neksbyte.porelgustoweb.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos generales de la documentación de la API (Swagger UI en /swagger-ui.html). */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI apiPorElGusto(TiendaConfig tienda) {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Por el Gusto")
                        .version("1.0")
                        .description("""
                                API JSON de la tienda: catálogo y CRUD de las tablas (app móvil), registro e inicio de sesión
                                (`/api/auth`, regresa un JWT), sugerencias del buscador, ubicación del repartidor, rastreo y
                                confirmación de entrega con el QR del pedido.

                                Las rutas de telemetría y entrega con QR usan la sesión del personal (inicia sesión en el sitio).""")
                        .contact(new Contact().name("Por el Gusto").email(tienda.getCorreo())))
                .components(new Components().addSecuritySchemes("bearer",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .description("Token de POST /api/auth/login")));
    }
}
