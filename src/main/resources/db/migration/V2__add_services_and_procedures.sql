-- ============================================================
-- Dentvision — Migración V2: servicios, procedimientos e insumos
-- ============================================================

CREATE TABLE IF NOT EXISTS servicios (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(200) NOT NULL,
    descripcion          TEXT,
    precio               DECIMAL(12,2),
    duracion_minutos     INT,
    estado               VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME
);

CREATE TABLE IF NOT EXISTS procedimientos (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(200) NOT NULL,
    descripcion          TEXT,
    costo                DECIMAL(12,2),
    estado               VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME
);

CREATE TABLE IF NOT EXISTS insumos (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(200) NOT NULL,
    descripcion          TEXT,
    unidad_medida        VARCHAR(50),
    stock_actual         INT NOT NULL DEFAULT 0,
    stock_minimo         INT NOT NULL DEFAULT 0,
    precio_unitario      DECIMAL(12,2),
    estado               VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion       DATETIME,
    fecha_actualizacion  DATETIME
);
