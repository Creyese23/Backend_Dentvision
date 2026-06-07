# Mejoras aplicadas al Backend Dentvision

## PRIORIDAD ALTA — Implementadas

### 1. Swagger / OpenAPI
- **`config/OpenApiConfig.java`** — metadata global: título, versión, contacto, servers (dev/prod), esquema de seguridad Bearer JWT.
- **`controller/PatientController.java`** — anotaciones `@Tag`, `@Operation`, `@ApiResponse` en todos los endpoints.
- **`application.properties`** — rutas y opciones de Swagger configuradas:
  - UI: `http://localhost:8080/swagger-ui/index.html`
  - JSON spec: `http://localhost:8080/v3/api-docs`
- SecurityConfig ya permite `/swagger-ui/**` y `/v3/api-docs/**` sin token.

**Aplicar el mismo patrón a los otros controladores**: copiar las anotaciones de `PatientController` en `AppointmentController`, `UserController`, etc.

---

### 2. Testing
Estructura creada en `src/test/`:

| Archivo | Tipo | Qué prueba |
|---|---|---|
| `service/PatientServiceTest.java` | Unit (Mockito) | lógica de negocio aislada del contexto |
| `controller/PatientControllerTest.java` | Web (`@WebMvcTest`) | HTTP 200/201/400/401/403/404 |
| `security/JwtServiceTest.java` | Unit puro | generación/validación/expiración de tokens |

`src/test/resources/application-test.properties` — H2 en memoria, Flyway deshabilitado.

**Para ejecutar:**
```bash
./mvnw test
```

**Para replicar a otros módulos:** duplica `PatientServiceTest` y `PatientControllerTest`, cambia la clase y ajusta los mocks.

---

### 3. Paginación
- **`dto/common/PageResponse.java`** — record genérico con `content`, `page`, `size`, `totalElements`, `totalPages`, `last`.
- **`service/PatientService.java`** — método `findAll(Pageable)` usando `Page<T>` de Spring Data.
- **`controller/PatientController.java`** — parámetros `page`, `size`, `sort`, `direction` en `GET /pacientes`.

**Llamada paginada:**
```
GET /pacientes?page=0&size=20&sort=apellidos&direction=asc
```

**Respuesta:**
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "last": false
}
```

**Para aplicar a otros controladores:** inyectar `Pageable` igual que en `PatientController`.

---

### 4. Validaciones
- Validaciones Jakarta (`@NotBlank`, `@NotNull`, `@Size`) ya estaban en los DTOs — se mantienen.
- **`validation/ValidEnum.java`** + **`validation/ValidEnumValidator.java`** — anotación personalizada para validar valores de enums en texto.

**Uso de `@ValidEnum`:**
```java
@ValidEnum(
    allowed = {"PENDIENTE", "CONFIRMADA", "CANCELADA"},
    message = "Estado debe ser PENDIENTE, CONFIRMADA o CANCELADA"
)
String estado;
```

- `GlobalExceptionHandler` ya maneja `MethodArgumentNotValidException` y devuelve mapa `{campo: mensaje}`.

---

### 5. Mapper automático (MapStruct)
- **`pom.xml`** — dependencia `mapstruct` + `mapstruct-processor` + `lombok-mapstruct-binding`.
- **`mapper/PatientMapper.java`** — mapper completo con `toResponse`, `toEntity`, `updateEntity`.
- **`mapper/AppointmentMapper.java`** — mapper para citas (patrón idéntico).
- **`mapper/UserMapper.java`** — mapper para usuarios (password excluida de respuesta).
- **`service/PatientService.java`** — usa `patientMapper.toResponse()` en lugar del `mapToResponse()` manual.

**Para crear un mapper nuevo:**
```java
@Mapper(componentModel = "spring")
public interface MiEntidadMapper {
    MiEntidadResponse toResponse(MiEntidad entity);
    @Mapping(target = "id", ignore = true)
    MiEntidad toEntity(MiEntidadRequest request);
    void updateEntity(MiEntidadRequest request, @MappingTarget MiEntidad entity);
}
```

---

## PRIORIDAD MEDIA — Implementadas

### 6. Flyway
- **`pom.xml`** — `flyway-core` + `flyway-mysql`.
- **`db/migration/V1__initial_schema.sql`** — tablas: roles, usuarios, pacientes, empleados, citas, refresh_tokens + datos semilla de roles.
- **`db/migration/V2__add_services_and_procedures.sql`** — servicios, procedimientos, insumos.
- **`application.properties`** — `spring.flyway.enabled=true`, `baseline-on-migrate=true`.
- `spring.jpa.hibernate.ddl-auto=validate` — Flyway es el responsable del schema.

**Agregar migraciones:** crear `V3__descripcion.sql`, `V4__descripcion.sql`, etc.

---

### 7. Logs
- `@Slf4j` en `PatientService` — logs DEBUG en consultas, INFO en creación/actualización/eliminación.
- `application.properties` — patrón de log con timestamp, nivel, hilo y clase.
- Configuración lista para archivo de log (comentada — descomentar en producción).

**Patrón a replicar en todos los servicios:**
```java
@Slf4j
@Service
public class MiServicio {
    public void create(...) {
        // lógica...
        log.info("Entidad creada id={}", saved.getId());
    }
}
```

---

### 8. Refresh Tokens
- **`entity/RefreshToken.java`** — entidad con token UUID, usuario, expiración y flag `revoked`.
- **`repository/RefreshTokenRepository.java`** — `findByToken` + `revokeAllByUser`.
- **`service/RefreshTokenService.java`** — `create` (con rotación), `validate`, `revokeAll`.
- **`controller/AuthController.java`** — endpoints:
  - `POST /auth/refresh` — renueva accessToken con refreshToken válido
  - `POST /auth/logout` — revoca todos los refresh tokens del usuario
- **`dto/auth/RefreshTokenRequest.java`** — DTO de request.
- **`dto/auth/AuthResponse.java`** — ahora incluye `refreshToken` y `expiresIn`.
- Variable de entorno: `JWT_REFRESH_EXPIRATION_MS=604800000` (7 días).

---

## PRIORIDAD FUTURA — No implementadas (pendiente)

### Redis
- Cachear resultados de consultas frecuentes (listas, búsquedas).
- Almacenar refresh tokens en Redis en lugar de BD (más rápido para validación).
- Implementar rate limiting por IP.
- Dependencia a agregar: `spring-boot-starter-data-redis`.

### Microservicios
- Extraer módulos (pacientes, facturación, inventario) como servicios independientes.
- Comunicación vía REST (OpenFeign) o mensajería (RabbitMQ / Kafka).
- Service discovery con Eureka o Kubernetes.
- API Gateway con Spring Cloud Gateway.

---

## Cómo aplicar los cambios a los módulos restantes

Para cada entidad (Appointment, Employee, Invoice, etc.):
1. Crear `mapper/XxxMapper.java` siguiendo el patrón de `PatientMapper`.
2. Inyectar el mapper en el servicio y eliminar el método `mapToResponse()` manual.
3. Agregar `findAll(Pageable)` en el servicio.
4. Agregar `@Tag`, `@Operation`, `@ApiResponse` en el controlador.
5. Agregar `@PreAuthorize` según el rol que debe tener acceso.
6. Crear `service/XxxServiceTest.java` y `controller/XxxControllerTest.java`.
