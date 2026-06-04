package co.edu.sena.Dentvision_Backend.dto.delivery;

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
public class DeliveryRequest {

    @NotNull(message = "La fecha de entrega es requerida")
    private LocalDate fechaEntrega;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    private String observaciones;

    @NotNull(message = "El ID de la orden es requerido")
    private Long idOrden;
}
