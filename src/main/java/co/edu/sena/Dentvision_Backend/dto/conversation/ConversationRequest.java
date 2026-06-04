package co.edu.sena.Dentvision_Backend.dto.conversation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotNull(message = "El ID del paciente es requerido")
    private Long idPaciente;

    @NotNull(message = "El ID del procedimiento es requerido")
    private Long idProcedimiento;
}
