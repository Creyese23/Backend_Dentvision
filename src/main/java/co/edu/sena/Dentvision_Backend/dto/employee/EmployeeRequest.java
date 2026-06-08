package co.edu.sena.Dentvision_Backend.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORRECCIÓN: se añadió el campo idUsuario, requerido por EmployeeService.create()
 * ya que Employee tiene una relación @OneToOne(optional=false) con User.
 * Sin este campo el servicio no podía resolver el usuario y la entidad
 * fallaba con violación de NOT NULL al guardar.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotNull(message = "El ID del usuario asociado es requerido")
    private Long idUsuario;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 3, max = 120, message = "El apellido debe tener entre 3 y 120 caracteres")
    private String apellidos;

    @NotBlank(message = "El documento es requerido")
    @Size(min = 5, max = 50, message = "El documento debe tener entre 5 y 50 caracteres")
    private String identificacion;

    @Size(max = 40, message = "El teléfono no puede exceder 40 caracteres")
    private String telefono;

    @Size(max = 40, message = "El teléfono no puede exceder 40 caracteres")
    private String especialidad;

    private String estado;
}
