package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA para la tabla `roles` de empleados.
 *
 * CORRECCIÓN: RoleService y data.sql referenciaban una tabla `roles` con una
 * entidad JPA llamada RoleEntity que no existía. Se crea aquí para que
 * RoleService, RoleEntityRepository y el seed de data.sql funcionen
 * correctamente.
 *
 * NOTA: Esta entidad es distinta del enum Role (que define los roles de
 * usuario del sistema). RoleEntity representa los roles clínicos/laborales
 * que se asignan a los empleados (ODONTOLOGO, TECNICO_DENTAL, etc.).
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    private NombreRol nombreRol;

    /** Roles clínicos/laborales disponibles para empleados. */
    public enum NombreRol {
        ODONTOLOGO,
        TECNICO_DENTAL,
        AUXILIAR_ADMINISTRATIVA
    }
}
