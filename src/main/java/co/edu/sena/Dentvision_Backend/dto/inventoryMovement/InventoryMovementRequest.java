package co.edu.sena.Dentvision_Backend.dto.inventoryMovement;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementRequest {

    @NotBlank(message = "El tipo de movimiento es requerido")
    private String tipoMovimiento;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;

    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;

    @NotNull(message = "El ID del insumo es requerido")
    private Long idInsumo;

    @NotNull(message = "El ID del empleado es requerido")
    private Long idEmpleado;
}
