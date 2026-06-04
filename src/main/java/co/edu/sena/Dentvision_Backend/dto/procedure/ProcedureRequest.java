package co.edu.sena.Dentvision_Backend.dto.procedure;

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
public class ProcedureRequest {

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String observaciones;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    @NotNull(message = "El ID de la cita es requerido")
    private Long idCita;

    @NotNull(message = "El ID del técnico es requerido")
    private Long idTecnico;
}
