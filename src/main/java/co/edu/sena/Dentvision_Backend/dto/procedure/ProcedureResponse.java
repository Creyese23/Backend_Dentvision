package co.edu.sena.Dentvision_Backend.dto.procedure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureResponse {

    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String observaciones;
    private String estado;
    private Long idCita;
    private Long idTecnico;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
