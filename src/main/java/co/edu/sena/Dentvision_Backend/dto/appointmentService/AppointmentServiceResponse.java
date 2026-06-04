package co.edu.sena.Dentvision_Backend.dto.appointmentService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentServiceResponse {

    private Long id;
    private Long idCita;
    private Long idServicio;
    private BigDecimal precioAcordado;
}
