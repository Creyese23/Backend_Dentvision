# Dentvision API Backend

API REST en **Spring Boot 3.5.13** + **Java 21** para el proyecto de backend de Dentvision.

Este backend administra usuarios, pacientes, citas y servicios, con seguridad JWT y soporte para MySQL o SQL Server.

---

## Características

- **Spring Boot 3.5.13** + **Java 21**
- Paquete base: `co.edu.sena.Dentvision_Backend`
- **4 perfiles** de ejecución:
  - `mysql-dev`, `mysql-prod`
  - `sqlserver-dev`, `sqlserver-prod`
- **Spring Security + JWT**
- Autenticación con tokens Bearer
- Contraseñas con **BCrypt**
- Gestión de errores centralizada
- **CORS abierto** para desarrollo
- Validación de entrada con `jakarta.validation`
- Seed inicial para usuario `admin` y servicios de ejemplo

---

## Estructura del proyecto

```
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   └── main/
│       ├── java/
│       │   └── co/
│       │       └── edu/
│       │           └── sena/
│       │               └── Dentvision_Backend/
│       │                   ├── config/
│       │                   │   ├── JpaAuditingConfig.java
│       │                   │   └── SecurityConfig.java
│       │                   ├── controller/
│       │                   │   ├── AppointmentController.java
│       │                   │   ├── AuthController.java
│       │                   │   ├── HomeController.java
│       │                   │   ├── PatientController.java
│       │                   │   └── ServiceController.java
│       │                   ├── dto/
│       │                   │   ├── appointment/
│       │                   │   ├── auth/
│       │                   │   ├── patient/
│       │                   │   └── service/
│       │                   ├── entity/
│       │                   ├── exception/
│       │                   ├── repository/
│       │                   ├── security/
│       │                   ├── service/
│       │                   └── DentvisionApiApplication.java
│       └── resources/
│           ├── application-mysql-dev.properties
│           ├── application-mysql-prod.properties
│           ├── application-sqlserver-dev.properties
│           ├── application-sqlserver-prod.properties
│           ├── application.properties
│           └── data.sql
├── .env
├── .gitattributes
├── .gitignore
├── Dockerfile
├── README.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## Requisitos

- JDK 21
- Maven 3.9+
- MySQL 8+ o SQL Server 2019+ corriendo localmente

---

## Puesta en marcha

### 1) Configurar el `.env`

Edita el archivo `.env` y completa las credenciales de la base de datos.

> Si prefieres, puedes copiar un ejemplo con `cp .env.example .env` cuando exista un archivo de plantilla.

### 2) Elegir perfil

En el `.env`:

```bash
SPRING_PROFILES_ACTIVE=mysql-dev
```

Valores válidos:
- `mysql-dev`
- `mysql-prod`
- `sqlserver-dev`
- `sqlserver-prod`

### 3) Arrancar la aplicación

En Linux/macOS:
```bash
./mvnw spring-boot:run
```

En Windows PowerShell:
```powershell
./mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### 4) Usuario semilla (perfiles `-dev`)

El seed inicial crea automáticamente:

| Campo    | Valor               |
| -------- | ------------------- |
| username | `admin`             |
| email    | `admin@sena.edu.co` |
| password | `admin123`          |
| rol      | `ROLE_ADMIN`        |

También se crean servicios de ejemplo como `Limpieza dental`, `Caries y empaste` y `Blanqueamiento dental`.

---

## Endpoints

### Autenticación

| Método | Ruta               | Body                                 | Respuesta                |
| ------ | ------------------ | ------------------------------------ | ------------------------ |
| POST   | `/auth/register`   | `{ username, email, password }`      | `201 { token, user }`    |
| POST   | `/auth/login`      | `{ email, password }`                | `200 { token, user }`    |

### Servicios

| Método | Ruta               | Body                                           | Respuesta                            |
| ------ | ------------------ | ---------------------------------------------- | ------------------------------------ |
| GET    | `/servicios`       | —                                              | `200 [ ServiceResponse ]`            |
| GET    | `/servicios/{id}`  | —                                              | `200 ServiceResponse`                |
| POST   | `/servicios`       | `{ "nombre", "descripcion", "precio", "duracionEstimada" }` | `201 ServiceResponse`           |
| PUT    | `/servicios/{id}`  | `{ "nombre", "descripcion", "precio", "duracionEstimada" }` | `200 ServiceResponse`           |
| DELETE | `/servicios/{id}`  | —                                              | `204`                                |

### Pacientes

| Método | Ruta               | Body                                                                   | Respuesta                            |
| ------ | ------------------ | ---------------------------------------------------------------------- | ------------------------------------ |
| GET    | `/pacientes`       | —                                                                      | `200 [ PatientResponse ]`            |
| GET    | `/pacientes/{id}`  | —                                                                      | `200 PatientResponse`                |
| POST   | `/pacientes`       | `{ "idUsuario", "nombres", "apellidos", "documento", "telefono", "direccion", "fechaNacimiento" }` | `200 PatientResponse` |
| PUT    | `/pacientes/{id}`  | `{ "idUsuario", "nombres", "apellidos", "documento", "telefono", "direccion", "fechaNacimiento" }` | `200 PatientResponse` |
| DELETE | `/pacientes/{id}`  | —                                                                      | `204`                                |

### Citas

| Método | Ruta              | Body                                                   | Respuesta                            |
| ------ | ----------------- | ------------------------------------------------------ | ------------------------------------ |
| GET    | `/citas`          | —                                                      | `200 [ AppointmentResponse ]`        |
| GET    | `/citas/{id}`     | —                                                      | `200 AppointmentResponse`            |
| POST   | `/citas`          | `{ "idPaciente", "idOdontologo", "fechaHora", "motivo" }` | `200 AppointmentResponse` |
| PUT    | `/citas/{id}`     | `{ "idPaciente", "idOdontologo", "fechaHora", "motivo" }` | `200 AppointmentResponse` |
| DELETE | `/citas/{id}`     | —                                                      | `204`                                |

> Nota: los endpoints de `pacientes` y `citas` requieren JWT válido.
>
> - `fechaNacimiento` debe enviarse como `YYYY-MM-DD`.
> - `fechaHora` debe enviarse como `YYYY-MM-DDTHH:mm:ss`.

---

## Seguridad

- `/auth/**` y `/servicios/**` son públicos.
- El resto de rutas requiere JWT válido en `Authorization: Bearer <token>`.
- Los tokens se firman con HS256 usando `JWT_SECRET`.
- Las contraseñas se almacenan con **BCrypt**.
- CORS está habilitado para todos los orígenes en desarrollo.

---

## Pruebas con curl

Login:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@sena.edu.co","password":"admin123"}'
```

Listar servicios públicos:
```bash
curl http://localhost:8080/servicios
```

Listar pacientes (requiere token):
```bash
curl http://localhost:8080/pacientes \
  -H "Authorization: Bearer <TOKEN>"
```

---

## Manejo de errores

Todos los errores siguen un formato uniforme para facilitar la lectura del frontend.

| Caso                     | Status HTTP |
| ------------------------ | ----------- |
| Validación de campos     | 400         |
| JSON mal formado         | 400         |
| Credenciales inválidas   | 401         |
| Recurso no encontrado    | 404         |
| Username/email duplicado | 409         |
| Error inesperado         | 500         |

---

## Notas

- El archivo `.env` NO debe commitearse; está en `.gitignore`.
- En los perfiles `-prod` se desactiva `data.sql` y `spring.jpa.hibernate.ddl-auto=validate`.
- Si cambias `JWT_SECRET`, los tokens existentes dejarán de ser válidos.
