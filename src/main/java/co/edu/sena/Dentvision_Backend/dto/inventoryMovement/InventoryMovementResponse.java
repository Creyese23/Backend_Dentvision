package co.edu.sena.Dentvision_Backend.dto.inventoryMovement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementResponse {

    private Long id;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDate fecha;
    private Long idInsumo;
    private Long idEmpleado;
}
