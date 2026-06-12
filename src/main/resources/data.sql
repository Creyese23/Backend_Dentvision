-- ============================================================
-- data.sql — Seed inicial de Dentvision
-- ============================================================

-- Usuario administrador inicial
INSERT INTO usuarios (tipo_identificacion, identificacion, nombres, apellidos, email, password, role, estado, created_at, updated_at)
SELECT 'CC', '1000000000', 'Admin', 'Sistema',
       'admin@dentvision.co',
       '$2b$10$uNjp2NXJ6kkTAdcPhV0LD.8Ek4LweekLrtby90i1Ohs2xFkIP3Amm',
       'ADMIN', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE identificacion = '1000000000');

-- Servicios odontológicos de ejemplo
INSERT INTO servicios (nombre, descripcion, precio, duracion_estimada, estado, created_at, updated_at)
SELECT 'Limpieza dental', 'Limpieza profesional para eliminar placa y sarro', 120000.00, 45, 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE nombre = 'Limpieza dental');

INSERT INTO servicios (nombre, descripcion, precio, duracion_estimada, estado, created_at, updated_at)
SELECT 'Caries y empaste', 'Tratamiento de cavidades con material biocompatible', 180000.00, 60, 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE nombre = 'Caries y empaste');

INSERT INTO servicios (nombre, descripcion, precio, duracion_estimada, estado, created_at, updated_at)
SELECT 'Blanqueamiento dental', 'Tratamiento estético para aclarar el tono dental', 320000.00, 90, 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM servicios WHERE nombre = 'Blanqueamiento dental');

-- Roles clínicos de empleados (deben coincidir con RoleEntity.NombreRol)
INSERT IGNORE INTO roles (nombre_rol) VALUES ('ODONTOLOGO');
INSERT IGNORE INTO roles (nombre_rol) VALUES ('TECNICO_DENTAL');
INSERT IGNORE INTO roles (nombre_rol) VALUES ('AUXILIAR_ADMINISTRATIVA');
