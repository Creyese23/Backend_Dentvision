package co.edu.sena.Dentvision_Backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitarios para JwtService.
 * No requiere contexto de Spring — se instancia manualmente.
 */
class JwtServiceTest {

    // Clave Base64 de 256 bits valida para HS256
    private static final String SECRET =
            "dGVzdFNlY3JldEtleVBhcmFEZW50dmlzaW9uUHJveWVjdG8xMjM0NTY3ODkwMTIzNDU2";

    private JwtService  jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService  = new JwtService(SECRET, 3_600_000L);  // 1 hora
        userDetails = User.builder()
                .username("admin@dentvision.co")
                .password("hashed")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("generateToken produce un token no nulo y no vacio")
    void generateToken_returnsNonBlankString() {
        String token = jwtService.generateToken(userDetails);
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("extractUsername devuelve el username original del token")
    void extractUsername_matchesOriginalUsername() {
        String token    = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertThat(username).isEqualTo("admin@dentvision.co");
    }

    @Test
    @DisplayName("isTokenValid retorna true para token recien generado")
    void isTokenValid_freshToken_returnsTrue() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid retorna false para token expirado")
    void isTokenValid_expiredToken_returnsFalse() {
        JwtService shortLived = new JwtService(SECRET, -1L);  // expira en el pasado
        String expiredToken   = shortLived.generateToken(userDetails);
        assertThat(jwtService.isTokenValid(expiredToken, userDetails)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid retorna false para token de otro usuario")
    void isTokenValid_wrongUser_returnsFalse() {
        String token = jwtService.generateToken(userDetails);
        UserDetails other = User.builder()
                .username("otro@dentvision.co")
                .password("x")
                .roles("ADMIN")
                .build();
        assertThat(jwtService.isTokenValid(token, other)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid retorna false para token manipulado")
    void isTokenValid_tamperedToken_returnsFalse() {
        String token    = jwtService.generateToken(userDetails);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThat(jwtService.isTokenValid(tampered, userDetails)).isFalse();
    }
}
