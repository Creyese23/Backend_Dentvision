package co.edu.sena.Dentvision_Backend.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientRequest(
        @NotNull(message = "El id de usuario es obligatorio")
        Long idUsuario,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 120, message = "Los nombres no pueden superar los 120 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 120, message = "Los apellidos no pueden superar los 120 caracteres")
        String apellidos,

        @NotBlank(message = "El documento es obligatorio")
        @Size(max = 50, message = "El documento no puede superar los 50 caracteres")
        String documento,

        @Size(max = 40, message = "El teléfono no puede superar los 40 caracteres")
        String telefono,

        @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
        String direccion,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate fechaNacimiento
) {
}
