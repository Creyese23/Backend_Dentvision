package co.edu.sena.Dentvision_Backend.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull(message = "El id del paciente es obligatorio")
        Long idPaciente,

        @NotNull(message = "El id del odontólogo es obligatorio")
        Long idOdontologo,

        @NotNull(message = "La fecha y hora son obligatorias")
        LocalDateTime fechaHora,

        @NotBlank(message = "El motivo es obligatorio")
        String motivo
) {
}
