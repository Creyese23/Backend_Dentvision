-- ============================================================
-- Dentvision — Migración V2: servicios, procedimientos e insumos
-- ============================================================

-- Servicios odontológicos ofrecidos por la clínica
CREATE TABLE IF NOT EXISTS servicios (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(150)   NOT NULL,
    descripcion       VARCHAR(1000),
    precio            DECIMAL(12, 2) NOT NULL,
    duracion_estimada INT            NOT NULL,
    estado            VARCHAR(50)    NOT NULL DEFAULT 'ACTIVO',
    created_at        DATETIME,
    updated_at        DATETIME
);

-- Procedimientos clínicos realizados en una cita por un técnico
CREATE TABLE IF NOT EXISTS procedimientos (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio        DATE,
    fecha_fin           DATE,
    observaciones       VARCHAR(500),
    estado              VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME,
    id_cita             BIGINT NOT NULL,
    id_tecnico          BIGINT NOT NULL,
    CONSTRAINT fk_proc_cita    FOREIGN KEY (id_cita)    REFERENCES citas(id),
    CONSTRAINT fk_proc_tecnico FOREIGN KEY (id_tecnico) REFERENCES empleados(id)
);

-- Insumos / materiales del inventario
CREATE TABLE IF NOT EXISTS insumos (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(150)   NOT NULL,
    descripcion         VARCHAR(500),
    unidad_medida       VARCHAR(50),
    stock_actual        INT            NOT NULL DEFAULT 0,
    stock_minimo        INT            NOT NULL DEFAULT 0,
    precio_unitario     DECIMAL(12, 2),
    estado              VARCHAR(50)    NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME
);
