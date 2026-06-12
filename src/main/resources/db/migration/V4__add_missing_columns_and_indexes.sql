-- ============================================================
-- Dentvision — Migración V4: índices de rendimiento
-- Las columnas de facturas ya se crean en V3; solo se agregan índices.
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_usuarios_estado     ON usuarios(estado);
CREATE INDEX IF NOT EXISTS idx_pacientes_estado    ON pacientes(estado);
CREATE INDEX IF NOT EXISTS idx_empleados_estado    ON empleados(estado);
CREATE INDEX IF NOT EXISTS idx_citas_estado        ON citas(estado);
CREATE INDEX IF NOT EXISTS idx_citas_fecha_hora    ON citas(fecha_hora);
CREATE INDEX IF NOT EXISTS idx_servicios_estado    ON servicios(estado);
CREATE INDEX IF NOT EXISTS idx_insumos_estado      ON insumos(estado);
CREATE INDEX IF NOT EXISTS idx_facturas_estado     ON facturas(estado);
CREATE INDEX IF NOT EXISTS idx_pagos_estado        ON pagos(estado);
CREATE INDEX IF NOT EXISTS idx_procedimientos_estado ON procedimientos(estado);
