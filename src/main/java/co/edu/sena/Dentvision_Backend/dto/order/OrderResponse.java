package co.edu.sena.Dentvision_Backend.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private LocalDate fechaCreacion;
    private String descripcion;
    private String estado;
    private Long idProcedimiento;
}
