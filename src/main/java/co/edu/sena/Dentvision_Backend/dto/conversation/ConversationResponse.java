package co.edu.sena.Dentvision_Backend.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {

    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long idPaciente;
    private Long idProcedimiento;
}
