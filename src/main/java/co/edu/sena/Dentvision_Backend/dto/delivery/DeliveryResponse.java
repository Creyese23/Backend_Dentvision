package co.edu.sena.Dentvision_Backend.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponse {

    private Long id;
    private LocalDate fechaEntrega;
    private String estado;
    private String observaciones;
    private Long idOrden;
}
