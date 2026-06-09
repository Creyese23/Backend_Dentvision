package co.edu.sena.Dentvision_Backend.dto.user;

import co.edu.sena.Dentvision_Backend.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "El tipo de identificacion es requerido")
    @Size(max = 50, message = "El tipo de identificacion debe tener 50 caracteres")
    private String tipoIdentificacion;

    @NotBlank(message = "La identificacion de usuario es requerido")
    @Size(max = 10, message = "La identificacion debe tener 10 caracteres")
    private String identificacion;

    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(min = 3, max = 60, message = "El nombre debe tener entre 3 y 60 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido de usuario es requerido")
    @Size(min = 3, max = 60, message = "La identificacion debe tener entre 3 y 60 caracteres")
    private String apellidos;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    private String estado;

    private Role role;
}
