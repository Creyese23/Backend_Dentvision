package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 120)
    private String nombres;

    @Column(nullable = false, length = 120)
    private String apellidos;

    // unique = true: no puede existir dos empleados con el mismo documento
    @Column(nullable = false, length = 50, unique = true)
    private String documento;

    @Column(length = 40)
    private String telefono;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "ACTIVO";

    // Soft delete: se registra la fecha en lugar de borrar físicamente
    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Un empleado puede tener hasta 3 roles: ODONTOLOGO, TECNICO_DENTAL, AUXILIAR_ADMINISTRATIVA
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmployeeRole> roles = new ArrayList<>();
}
