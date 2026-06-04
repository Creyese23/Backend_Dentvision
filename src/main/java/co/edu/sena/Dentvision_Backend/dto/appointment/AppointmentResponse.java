package co.edu.sena.Dentvision_Backend.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long idPaciente,
        Long idOdontologo,
        LocalDateTime fechaHora,
        String motivo,
        String estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        LocalDateTime fechaEliminacion
) {
}
