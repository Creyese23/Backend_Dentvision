package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    /**
     * Roles válidos para un empleado dental.
     * Usar enum garantiza que solo se puedan insertar estos tres valores,
     * evitando inconsistencias como "odontólogo", "Odontologo", "odontologo", etc.
     */
    public enum NombreRol {
        ODONTOLOGO,
        TECNICO_DENTAL,
        AUXILIAR_ADMINISTRATIVA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Se almacena como String en la BD (ej: "ODONTOLOGO"), no como índice numérico
    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", nullable = false, length = 30, unique = true)
    private NombreRol nombreRol;
}
