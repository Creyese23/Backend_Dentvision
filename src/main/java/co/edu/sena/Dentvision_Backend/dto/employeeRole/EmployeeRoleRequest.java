package co.edu.sena.Dentvision_Backend.dto.employeeRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRoleRequest {

    @NotNull(message = "El ID del empleado es requerido")
    private Long idEmpleado;

    @NotNull(message = "El ID del rol es requerido")
    private Long idRol;
}
