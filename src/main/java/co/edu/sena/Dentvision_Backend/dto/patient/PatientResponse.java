package co.edu.sena.Dentvision_Backend.dto.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Long id,
        Long idUsuario,
        String nombres,
        String apellidos,
        String documento,
        String telefono,
        String direccion,
        LocalDate fechaNacimiento,
        String estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        LocalDateTime fechaEliminacion
) {
}
