package co.edu.sena.Dentvision_Backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI 3.
 *
 * Accede a la UI en: http://localhost:8080/swagger-ui/index.html
 * JSON de la spec:   http://localhost:8080/v3/api-docs
 *
 * Para probar endpoints protegidos:
 *   1. Haz POST /auth/login y copia el token.
 *   2. Haz clic en "Authorize" (candado) e ingresa: Bearer <token>
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title       = "Dentvision API",
        version     = "1.0.0",
        description = "API REST para el sistema de gestión odontológica Dentvision. "
                    + "Proyecto formativo SENA ADSO.",
        contact = @Contact(
            name  = "Equipo Dentvision",
            email = "dentvision@edu.co"
        ),
        license = @License(name = "MIT")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de desarrollo"),
        @Server(url = "https://api.dentvision.co", description = "Servidor de producción")
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name        = "bearerAuth",
    type        = SecuritySchemeType.HTTP,
    scheme      = "bearer",
    bearerFormat = "JWT",
    description = "Ingresa el token JWT obtenido en POST /auth/login"
)
public class OpenApiConfig {
    // springdoc-openapi auto-descubre controladores; aquí solo se define metadata global.
}
