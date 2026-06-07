package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.user.UserRequest;
import co.edu.sena.Dentvision_Backend.dto.user.UserResponse;
import co.edu.sena.Dentvision_Backend.entity.Role;
import co.edu.sena.Dentvision_Backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CORRECCIÓN DE SEGURIDAD CRÍTICA: la ruta /usuarios/** estaba en
 * permitAll() en SecurityConfig, exponiendo todos los endpoints sin
 * autenticación. Se movió a autenticación requerida y se añaden
 * @PreAuthorize por método para control granular de acceso.
 *
 * También se añade el endpoint PATCH /{id}/rol exclusivo para ADMIN.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /**
     * Cambio de rol explícito, solo ADMIN.
     * CORRECCIÓN: evita que cualquier usuario pueda escalar su propio rol
     * enviando el campo role en el body del PUT.
     */
    @PatchMapping("/{id}/rol")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        return ResponseEntity.ok(userService.changeRole(id, role));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
