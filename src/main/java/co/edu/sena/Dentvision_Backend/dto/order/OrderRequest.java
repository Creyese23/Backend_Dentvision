package co.edu.sena.Dentvision_Backend.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "La fecha de creación es requerida")
    private LocalDate fechaCreacion;

    private String descripcion;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    @NotNull(message = "El ID del procedimiento es requerido")
    private Long idProcedimiento;
}
