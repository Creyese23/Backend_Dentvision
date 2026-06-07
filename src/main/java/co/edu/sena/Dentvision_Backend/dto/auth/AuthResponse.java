package co.edu.sena.Dentvision_Backend.dto.auth;

/**
 * Respuesta de autenticacion con Access Token + Refresh Token.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long   expiresIn
) {
    public static AuthResponse of(String accessToken, String refreshToken, long expiresInMs) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresInMs / 1000);
    }
}
