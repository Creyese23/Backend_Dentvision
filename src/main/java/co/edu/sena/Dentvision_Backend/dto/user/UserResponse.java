package co.edu.sena.Dentvision_Backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CORRECCIÓN: se añadió el campo `role` que UserService ya devuelve
 * en mapToResponse() pero que faltaba en el DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
