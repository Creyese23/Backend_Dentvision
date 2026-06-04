package co.edu.sena.Dentvision_Backend.dto.appointmentService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentServiceRequest {

    @NotNull(message = "El ID de la cita es requerido")
    private Long idCita;

    @NotNull(message = "El ID del servicio es requerido")
    private Long idServicio;

    @NotNull(message = "El precio acordado es requerido")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precioAcordado;
}
