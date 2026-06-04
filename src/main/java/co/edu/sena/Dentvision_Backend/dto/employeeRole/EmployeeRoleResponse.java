package co.edu.sena.Dentvision_Backend.dto.employeeRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRoleResponse {

    private Long id;
    private Long idEmpleado;
    private Long idRol;
}
