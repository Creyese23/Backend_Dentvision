-- ============================================================
-- Dentvision — Migración V1: esquema inicial
-- Flyway ejecuta este script UNA SOLA VEZ en orden ascendente.
-- Convención de nombres: V{version}__{descripcion}.sql
-- ============================================================

-- Tabla de roles del sistema
CREATE TABLE IF NOT EXISTS roles (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    username             VARCHAR(100) NOT NULL UNIQUE,
    email                VARCHAR(150) NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    estado               VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    id_rol               BIGINT,
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME,
    fecha_eliminacion    DATETIME,
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (id_rol) REFERENCES roles(id)
);

-- Tabla de pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario           BIGINT NOT NULL UNIQUE,
    nombres              VARCHAR(120) NOT NULL,
    apellidos            VARCHAR(120) NOT NULL,
    documento            VARCHAR(50)  NOT NULL,
    telefono             VARCHAR(40),
    direccion            VARCHAR(250),
    fecha_nacimiento     DATE,
    estado               VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME,
    fecha_eliminacion    DATETIME,
    CONSTRAINT fk_pacientes_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Tabla de empleados
CREATE TABLE IF NOT EXISTS empleados (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario           BIGINT NOT NULL UNIQUE,
    nombres              VARCHAR(120) NOT NULL,
    apellidos            VARCHAR(120) NOT NULL,
    documento            VARCHAR(50)  NOT NULL,
    telefono             VARCHAR(40),
    cargo                VARCHAR(100),
    estado               VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME,
    fecha_eliminacion    DATETIME,
    CONSTRAINT fk_empleados_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Tabla de citas
CREATE TABLE IF NOT EXISTS citas (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente          BIGINT NOT NULL,
    id_odontologo        BIGINT NOT NULL,
    fecha_hora           DATETIME NOT NULL,
    motivo               VARCHAR(500),
    estado               VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME,
    CONSTRAINT fk_citas_paciente   FOREIGN KEY (id_paciente)   REFERENCES pacientes(id),
    CONSTRAINT fk_citas_odontologo FOREIGN KEY (id_odontologo) REFERENCES empleados(id)
);

-- Datos semilla de roles
INSERT IGNORE INTO roles (nombre) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_ODONTOLOGO'),
    ('ROLE_RECEPCIONISTA'),
    ('ROLE_PACIENTE');

-- Tabla de refresh tokens (PRIORIDAD MEDIA)
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    token        VARCHAR(512) NOT NULL UNIQUE,
    id_usuario   BIGINT NOT NULL,
    expires_at   DATETIME NOT NULL,
    revoked      TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_rt_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
