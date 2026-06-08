package co.edu.sena.Dentvision_Backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El tipo de identificacion es obligatorio")
        @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
        String tipoIdentificacion,

        @NotBlank(message = "La identificacion es obligatorio")
        @Size(max = 10, message = "El username debe tener 10 caracteres")
        String identificacion,

        @NotBlank(message = "El nombre de usuario es requerido")
        @Size(min = 3, max = 60, message = "El nombre debe tener entre 3 y 60 caracteres")
        String nombres,

        @NotBlank(message = "El apellido de usuario es requerido")
        @Size(min = 3, max = 60, message = "La identificacion debe tener entre 3 y 60 caracteres")
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        String password
) {
}
