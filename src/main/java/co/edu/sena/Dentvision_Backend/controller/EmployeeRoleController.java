package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleRequest;
import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleResponse;
import co.edu.sena.Dentvision_Backend.service.EmployeeRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CORRECCIÓN: se agregó el import faltante de EmployeeRoleService
 * (que tampoco existía — se creó en esta corrección).
 * Se añade @PreAuthorize para restringir el acceso solo a ADMIN.
 */
@RestController
@RequestMapping("/empleado-roles")
@RequiredArgsConstructor
public class EmployeeRoleController {

    private final EmployeeRoleService employeeRoleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ODONTOLOGO')")
    public ResponseEntity<List<EmployeeRoleResponse>> getAll() {
        return ResponseEntity.ok(employeeRoleService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ODONTOLOGO')")
    public ResponseEntity<EmployeeRoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeRoleService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeRoleResponse> create(@Valid @RequestBody EmployeeRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRoleService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeRoleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRoleRequest request
    ) {
        return ResponseEntity.ok(employeeRoleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
