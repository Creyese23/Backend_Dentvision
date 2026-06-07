package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Tabla de unión entre Empleado y RoleEntity (roles clínicos).
 *
 * CORRECCIÓN: EmployeeRoleController y EmployeeRoleRequest/Response
 * referenciaban esta entidad que no existía. Se crea aquí junto con
 * su repositorio y servicio para que el módulo compile.
 *
 * Un empleado puede tener múltiples roles clínicos.
 */
@Entity
@Table(
    name = "empleado_roles",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_empleado_rol",
        columnNames = {"id_empleado", "id_rol"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
