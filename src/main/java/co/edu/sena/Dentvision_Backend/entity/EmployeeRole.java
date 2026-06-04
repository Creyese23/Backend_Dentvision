package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
// Restricción única a nivel de BD: un empleado no puede tener el mismo rol dos veces
@Table(
    name = "empleados_roles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_empleado_rol",
            columnNames = {"id_empleado", "id_rol"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Employee empleado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private RoleEntity rol;
}
