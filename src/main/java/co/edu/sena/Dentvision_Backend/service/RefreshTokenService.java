package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.entity.RefreshToken;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Gestiona el ciclo de vida de los refresh tokens.
 *
 * Configuracion en .env / application.properties:
 *   app.jwt.refresh-expiration-ms=604800000   # 7 dias
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private long refreshExpirationMs;

    /**
     * Crea un nuevo refresh token para el usuario.
     * Revoca los anteriores para evitar tokens huerfanos (rotacion de tokens).
     */
    public RefreshToken create(User user) {
        // Revocar tokens anteriores antes de crear uno nuevo (token rotation)
        refreshTokenRepository.revokeAllByUser(user);

        RefreshToken rt = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshExpirationMs))
                .revoked(false)
                .build();

        RefreshToken saved = refreshTokenRepository.save(rt);
        log.debug("Refresh token creado para usuario id={}", user.getId());
        return saved;
    }

    /**
     * Valida el token y retorna la entidad.
     * Lanza excepcion si el token no existe, fue revocado o expiró.
     */
    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token no encontrado"));

        if (rt.isRevoked()) {
            throw new IllegalStateException("El refresh token ya fue revocado");
        }
        if (rt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("El refresh token ha expirado");
        }
        return rt;
    }

    /** Revoca todos los refresh tokens del usuario (logout). */
    public void revokeAll(User user) {
        refreshTokenRepository.revokeAllByUser(user);
        log.info("Todos los refresh tokens revocados para usuario id={}", user.getId());
    }
}
