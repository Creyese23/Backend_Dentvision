-- ============================================================
-- Dentvision — Migración V1: esquema inicial
-- ============================================================

-- Roles clínicos/laborales de empleados
CREATE TABLE IF NOT EXISTS roles (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

-- Usuarios del sistema (autenticación via enum role, sin FK a roles)
CREATE TABLE IF NOT EXISTS usuarios (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_identificacion VARCHAR(60)  NOT NULL,
    identificacion      VARCHAR(60)  NOT NULL,
    nombres             VARCHAR(60)  NOT NULL,
    apellidos           VARCHAR(60)  NOT NULL,
    email               VARCHAR(120) NOT NULL,
    password            VARCHAR(100) NOT NULL,
    role                VARCHAR(20)  NOT NULL,
    estado              VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    fecha_eliminacion   DATETIME,
    created_at          DATETIME,
    updated_at          DATETIME,
    CONSTRAINT uk_usuarios_identificacion UNIQUE (identificacion),
    CONSTRAINT uk_usuarios_email          UNIQUE (email)
);

-- Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario          BIGINT NOT NULL UNIQUE,
    nombres             VARCHAR(120) NOT NULL,
    apellidos           VARCHAR(120) NOT NULL,
    documento           VARCHAR(50)  NOT NULL,
    telefono            VARCHAR(40),
    direccion           VARCHAR(250),
    fecha_nacimiento    DATE,
    estado              VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME,
    CONSTRAINT fk_pacientes_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Empleados (odontólogos, técnicos, auxiliares)
CREATE TABLE IF NOT EXISTS empleados (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario        BIGINT NOT NULL UNIQUE,
    nombres           VARCHAR(120) NOT NULL,
    apellidos         VARCHAR(120) NOT NULL,
    identificacion    VARCHAR(50)  NOT NULL UNIQUE,
    telefono          VARCHAR(40),
    estado            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO',
    especialidad      VARCHAR(40),
    fecha_eliminacion DATETIME,
    created_at        DATETIME,
    updated_at        DATETIME,
    CONSTRAINT fk_empleados_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Citas odontológicas
CREATE TABLE IF NOT EXISTS citas (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente         BIGINT NOT NULL,
    id_odontologo       BIGINT NOT NULL,
    fecha_hora          DATETIME NOT NULL,
    motivo              VARCHAR(250),
    estado              VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME,
    CONSTRAINT fk_citas_paciente   FOREIGN KEY (id_paciente)   REFERENCES pacientes(id),
    CONSTRAINT fk_citas_odontologo FOREIGN KEY (id_odontologo) REFERENCES empleados(id)
);

-- Refresh tokens para autenticación JWT
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    token      VARCHAR(512) NOT NULL UNIQUE,
    id_usuario BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,
    revoked    TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_rt_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Seed: roles clínicos de empleados
INSERT IGNORE INTO roles (nombre_rol) VALUES
    ('ODONTOLOGO'),
    ('TECNICO_DENTAL'),
    ('AUXILIAR_ADMINISTRATIVA');
