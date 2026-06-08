package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.auth.AuthResponse;
import co.edu.sena.Dentvision_Backend.dto.auth.LoginRequest;
import co.edu.sena.Dentvision_Backend.dto.auth.RefreshTokenRequest;
import co.edu.sena.Dentvision_Backend.dto.auth.RegisterRequest;
import co.edu.sena.Dentvision_Backend.entity.RefreshToken;
import co.edu.sena.Dentvision_Backend.service.AuthService;
import co.edu.sena.Dentvision_Backend.service.RefreshTokenService;
import co.edu.sena.Dentvision_Backend.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticacion.
 *
 * Endpoints publicos (no requieren JWT):
 *   POST /auth/register  — registrar nuevo usuario
 *   POST /auth/login     — obtener accessToken + refreshToken
 *   POST /auth/refresh   — renovar accessToken con refreshToken
 *   POST /auth/logout    — revocar refresh tokens del usuario
 */
@Tag(name = "Autenticacion", description = "Registro, login, refresh y logout")
@SecurityRequirements   // estos endpoints no requieren bearerAuth en Swagger
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService          authService;
    private final RefreshTokenService  refreshTokenService;
    private final JwtService           jwtService;
    private final UserDetailsService   userDetailsService;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    @Operation(summary = "Registrar usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "409", description = "Email o username ya existen")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Login — obtiene accessToken y refreshToken")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
        summary     = "Renovar access token",
        description = "Usa el refreshToken para obtener un nuevo accessToken sin pedir credenciales."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token renovado"),
        @ApiResponse(responseCode = "400", description = "Refresh token invalido, revocado o expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken rt          = refreshTokenService.validate(request.refreshToken());
        UserDetails  userDetails = userDetailsService.loadUserByUsername(rt.getUser().getIdentificacion());

        String newAccessToken    = jwtService.generateToken(userDetails);
        RefreshToken newRt       = refreshTokenService.create(rt.getUser());  // rotacion

        return ResponseEntity.ok(AuthResponse.of(newAccessToken, newRt.getToken(), jwtExpirationMs));
    }

    @Operation(summary = "Logout — revoca los refresh tokens del usuario")
    @ApiResponse(responseCode = "204", description = "Logout exitoso")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken rt = refreshTokenService.validate(request.refreshToken());
        refreshTokenService.revokeAll(rt.getUser());
        return ResponseEntity.noContent().build();
    }
}
