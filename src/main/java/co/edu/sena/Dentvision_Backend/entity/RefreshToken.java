package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entidad que almacena los refresh tokens en BD.
 *
 * Flujo de Refresh Token:
 * 1. POST /auth/login  → devuelve {accessToken, refreshToken}
 * 2. accessToken expira (15-60 min)
 * 3. POST /auth/refresh  con {refreshToken}  → nuevo {accessToken, refreshToken}
 * 4. POST /auth/logout   con {refreshToken}  → elimina el token de BD
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;
}
