package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.user.UserRequest;
import co.edu.sena.Dentvision_Backend.dto.user.UserResponse;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.DuplicateResourceException;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static co.edu.sena.Dentvision_Backend.entity.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return mapToResponse(user);
    }

    public UserResponse create(UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(hashPassword(request.getPassword()))
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                .role(request.getRole() != null ? request.getRole() : ROLE_USER)
                .build();

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("El usuario existe en el sistema");
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("El email existe en el sistema");
        }

        return mapToResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(hashPassword(request.getPassword()));
        }
        if (request.getEstado() != null) {
            user.setEstado(request.getEstado());
        }

        return mapToResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        user.setEstado("INACTIVO");
        userRepository.save(user);
    }

    private String hashPassword(String password) {
        if (password != null && password.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$")) {
            return password;
        }
        return passwordEncoder.encode(password);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .estado(user.getEstado())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
