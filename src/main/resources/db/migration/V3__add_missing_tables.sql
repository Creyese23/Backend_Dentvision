-- ============================================================
-- Dentvision — Migración V3: tablas faltantes
-- ============================================================

-- Servicios asignados a una cita (precio negociado por cita)
CREATE TABLE IF NOT EXISTS citas_servicios (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita         BIGINT         NOT NULL,
    id_servicio     BIGINT         NOT NULL,
    precio_acordado DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_cs_cita     FOREIGN KEY (id_cita)     REFERENCES citas(id),
    CONSTRAINT fk_cs_servicio FOREIGN KEY (id_servicio) REFERENCES servicios(id)
);

-- Facturas emitidas a pacientes
CREATE TABLE IF NOT EXISTS facturas (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente         BIGINT NOT NULL,
    fecha_emision       DATE,
    fecha_vencimiento   DATE,
    estado              VARCHAR(50)  NOT NULL DEFAULT 'PENDIENTE',
    descripcion         VARCHAR(500),
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME,
    CONSTRAINT fk_facturas_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id)
);

-- Detalle de ítems de una factura
CREATE TABLE IF NOT EXISTS detalle_factura (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_factura      BIGINT         NOT NULL,
    id_servicio     BIGINT         NOT NULL,
    cantidad        INT            NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    subtotal        DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_df_factura  FOREIGN KEY (id_factura)  REFERENCES facturas(id),
    CONSTRAINT fk_df_servicio FOREIGN KEY (id_servicio) REFERENCES servicios(id)
);

-- Pagos realizados contra una factura
CREATE TABLE IF NOT EXISTS pagos (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_factura          BIGINT         NOT NULL,
    fecha_pago          DATE,
    metodo_pago         VARCHAR(100),
    valor               DECIMAL(12, 2) NOT NULL,
    estado              VARCHAR(50)    NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion      DATETIME,
    fecha_actualizacion DATETIME,
    fecha_eliminacion   DATETIME,
    CONSTRAINT fk_pagos_factura FOREIGN KEY (id_factura) REFERENCES facturas(id)
);

-- Conversaciones clínicas asociadas a un procedimiento
CREATE TABLE IF NOT EXISTS conversaciones (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio     DATE,
    fecha_fin        DATE,
    id_paciente      BIGINT NOT NULL,
    id_procedimiento BIGINT NOT NULL,
    CONSTRAINT fk_conv_paciente      FOREIGN KEY (id_paciente)      REFERENCES pacientes(id),
    CONSTRAINT fk_conv_procedimiento FOREIGN KEY (id_procedimiento) REFERENCES procedimientos(id)
);

-- Mensajes dentro de una conversación
CREATE TABLE IF NOT EXISTS mensajes (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenido       VARCHAR(1000) NOT NULL,
    remitente       VARCHAR(100)  NOT NULL,
    fecha_hora      DATETIME      NOT NULL,
    id_conversacion BIGINT        NOT NULL,
    CONSTRAINT fk_msg_conversacion FOREIGN KEY (id_conversacion) REFERENCES conversaciones(id)
);

-- Órdenes de trabajo generadas desde un procedimiento
CREATE TABLE IF NOT EXISTS ordenes (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion      VARCHAR(250),
    estado           VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion   DATE,
    id_procedimiento BIGINT NOT NULL,
    CONSTRAINT fk_orden_procedimiento FOREIGN KEY (id_procedimiento) REFERENCES procedimientos(id)
);

-- Detalle de servicios en una orden
CREATE TABLE IF NOT EXISTS orden_detalle (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_orden        BIGINT         NOT NULL,
    id_servicio     BIGINT         NOT NULL,
    cantidad        INT            NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    observaciones   VARCHAR(500),
    CONSTRAINT fk_od_orden    FOREIGN KEY (id_orden)    REFERENCES ordenes(id),
    CONSTRAINT fk_od_servicio FOREIGN KEY (id_servicio) REFERENCES servicios(id)
);

-- Entregas asociadas a una orden
CREATE TABLE IF NOT EXISTS entregas (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_entrega DATE,
    estado        VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    observaciones VARCHAR(500),
    id_orden      BIGINT NOT NULL,
    CONSTRAINT fk_entregas_orden FOREIGN KEY (id_orden) REFERENCES ordenes(id)
);

-- Roles clínicos asignados a empleados (junction table)
CREATE TABLE IF NOT EXISTS empleado_roles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_empleado BIGINT NOT NULL,
    id_rol      BIGINT NOT NULL,
    created_at  DATETIME,
    updated_at  DATETIME,
    CONSTRAINT uk_empleado_rol  UNIQUE (id_empleado, id_rol),
    CONSTRAINT fk_er_empleado   FOREIGN KEY (id_empleado) REFERENCES empleados(id),
    CONSTRAINT fk_er_rol        FOREIGN KEY (id_rol)      REFERENCES roles(id)
);

-- Movimientos de inventario (entradas/salidas de insumos)
CREATE TABLE IF NOT EXISTS movimientos_inventario (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_movimiento VARCHAR(50) NOT NULL,
    cantidad        INT         NOT NULL,
    fecha           DATE        NOT NULL,
    id_insumo       BIGINT      NOT NULL,
    id_empleado     BIGINT      NOT NULL,
    CONSTRAINT fk_mi_insumo   FOREIGN KEY (id_insumo)   REFERENCES insumos(id),
    CONSTRAINT fk_mi_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id)
);
