package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleRequest;
import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleResponse;
import co.edu.sena.Dentvision_Backend.service.EmployeeRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleado-roles")
@RequiredArgsConstructor
public class EmployeeRoleController {

    private final EmployeeRoleService employeeRoleService;

    @GetMapping
    public ResponseEntity<List<EmployeeRoleResponse>> getAll() {
        return ResponseEntity.ok(employeeRoleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeRoleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeRoleResponse> create(@Valid @RequestBody EmployeeRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRoleService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRoleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRoleRequest request
    ) {
        return ResponseEntity.ok(employeeRoleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
