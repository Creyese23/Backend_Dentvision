package co.edu.sena.Dentvision_Backend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORRECCIÓN: se añadió idUsuario para que la respuesta incluya
 * la referencia al usuario asociado al empleado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String documento;
    private String telefono;
    private String estado;
}
