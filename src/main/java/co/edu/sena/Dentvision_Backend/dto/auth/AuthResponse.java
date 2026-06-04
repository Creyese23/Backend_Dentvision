package co.edu.sena.Dentvision_Backend.dto.auth;

public record AuthResponse(
        String token,
        UserDto user
) {
}
