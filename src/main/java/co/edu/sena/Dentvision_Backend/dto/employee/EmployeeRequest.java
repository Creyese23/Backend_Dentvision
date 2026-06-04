package co.edu.sena.Dentvision_Backend.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 3, max = 120, message = "El apellido debe tener entre 3 y 120 caracteres")
    private String apellidos;

    @NotBlank(message = "El documento es requerido")
    @Size(min = 5, max = 50, message = "El documento debe tener entre 5 y 50 caracteres")
    private String documento;

    @Size(max = 40, message = "El teléfono no puede exceder 40 caracteres")
    private String telefono;

    private String estado;
}
