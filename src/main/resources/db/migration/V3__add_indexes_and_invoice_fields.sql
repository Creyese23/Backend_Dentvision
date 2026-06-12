-- Índices para columnas frecuentemente consultadas
CREATE INDEX IF NOT EXISTS idx_usuarios_email          ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuarios_identificacion ON usuarios(identificacion);
CREATE INDEX IF NOT EXISTS idx_usuarios_estado         ON usuarios(estado);

CREATE INDEX IF NOT EXISTS idx_pacientes_estado        ON pacientes(estado);

CREATE INDEX IF NOT EXISTS idx_empleados_estado        ON empleados(estado);
CREATE INDEX IF NOT EXISTS idx_empleados_identificacion ON empleados(identificacion);

CREATE INDEX IF NOT EXISTS idx_citas_estado            ON citas(estado);
CREATE INDEX IF NOT EXISTS idx_citas_fecha_hora        ON citas(fecha_hora);

CREATE INDEX IF NOT EXISTS idx_servicios_estado        ON servicios(estado);

CREATE INDEX IF NOT EXISTS idx_insumos_estado          ON insumos(estado);

CREATE INDEX IF NOT EXISTS idx_procedimientos_fecha_eliminacion ON procedimientos(fecha_eliminacion);

-- Campos faltantes en la tabla facturas (Invoice entity)
ALTER TABLE facturas
    ADD COLUMN IF NOT EXISTS fecha_vencimiento DATE,
    ADD COLUMN IF NOT EXISTS estado            VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    ADD COLUMN IF NOT EXISTS descripcion       VARCHAR(500);
