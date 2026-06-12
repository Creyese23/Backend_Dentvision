package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.user.UserRequest;
import co.edu.sena.Dentvision_Backend.dto.user.UserResponse;
import co.edu.sena.Dentvision_Backend.entity.Role;
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

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        return mapToResponse(user);
    }

    public UserResponse create(UserRequest request) {
        if (userRepository.existsByIdentificacion(request.getIdentificacion()) || userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Ya existe este usuario");
        }

        User user = User.builder()
                .tipoIdentificacion(request.getTipoIdentificacion())
                .identificacion(request.getIdentificacion())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(hashPassword(request.getPassword()))
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                // CORRECCIÓN: era Role.ROLE_USER (inexistente). Ahora Role.USER.
                // El rol asignado desde la API solo se acepta si viene en el request;
                // de lo contrario se asigna USER por defecto.
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    /**
     * Actualización sin cambio de rol.
     * CORRECCIÓN DE SEGURIDAD: el campo `role` fue eliminado de la
     * actualización para evitar escalada de privilegios. Solo ADMIN puede
     * cambiar roles, mediante el endpoint dedicado changeRole().
     */
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setIdentificacion(request.getIdentificacion());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(hashPassword(request.getPassword()));
        }
        if (request.getEstado() != null) {
            user.setEstado(request.getEstado());
        }
        // Nota: el rol NO se actualiza aquí para evitar escalada de privilegios.
        // Usar changeRole() (solo ADMIN) para cambiar el rol.

        return mapToResponse(userRepository.save(user));
    }

    /**
     * Cambia el rol de un usuario. Solo debe ser llamado por ADMIN
     * (la restricción se aplica en el controlador con @PreAuthorize).
     */
    public UserResponse changeRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setRole(newRole);
        return mapToResponse(userRepository.save(user));
    }

    /** Soft delete: marca como INACTIVO en lugar de borrar físicamente. */
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
        user.setEstado("INACTIVO");
        userRepository.save(user);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .tipoIdentificacion(user.getTipoIdentificacion())
                .identificacion(user.getIdentificacion())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .estado(user.getEstado())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
