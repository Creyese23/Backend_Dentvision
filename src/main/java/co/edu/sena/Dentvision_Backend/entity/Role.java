package co.edu.sena.Dentvision_Backend.entity;

/**
 * Roles del sistema.
 * Se persisten como STRING en la columna `role` de `usuarios`.
 * Spring Security los usa como authority directamente (sin prefijo ROLE_).
 *
 * CORRECCIÓN: eliminado prefijo ROLE_ que causaba IllegalArgumentException
 * en AuthService y UserService al hacer Role.ROLE_USER (valor inexistente).
 */
public enum Role {
    USER,
    ADMIN,
    ODONTOLOGO,
    TECNICO_DENTAL,
    AUXILIAR_ADMINISTRATIVA
}
