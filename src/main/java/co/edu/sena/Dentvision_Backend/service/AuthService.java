package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.auth.AuthResponse;
import co.edu.sena.Dentvision_Backend.dto.auth.LoginRequest;
import co.edu.sena.Dentvision_Backend.dto.auth.RegisterRequest;
import co.edu.sena.Dentvision_Backend.entity.RefreshToken;
import co.edu.sena.Dentvision_Backend.entity.Role;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.DuplicateResourceException;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import co.edu.sena.Dentvision_Backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByIdentificacion(request.identificacion())
                || userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("El usuario ya está registrado");
        }

        User user = User.builder()
                .tipoIdentificacion(request.tipoIdentificacion())
                .identificacion(request.identificacion())
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        UserDetails userDetails = buildUserDetails(saved);
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.create(saved);

        return AuthResponse.of(accessToken, refreshToken.getToken(),
                jwtService.getExpirationMs());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RefreshToken refreshToken = refreshTokenService.create(user);

        return AuthResponse.of(accessToken, refreshToken.getToken(),
                jwtService.getExpirationMs());
    }

    private UserDetails buildUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
