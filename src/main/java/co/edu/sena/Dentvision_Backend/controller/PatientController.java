package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.common.PageResponse;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Pacientes.
 *
 * Seguridad con @PreAuthorize:
 *   - ROLE_ADMIN y ROLE_ODONTOLOGO pueden leer y modificar.
 *   - ROLE_ADMIN puede eliminar.
 *
 * Paginación:
 *   GET /pacientes?page=0&size=20&sort=apellidos,asc
 */
@Tag(name = "Pacientes", description = "Gestión de pacientes del sistema Dentvision")
@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // ─── GET paginado ─────────────────────────────────────────────────────────

    @Operation(
        summary     = "Listar pacientes paginados",
        description = "Devuelve una página de pacientes. Usa los parámetros `page`, `size` y `sort`."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ODONTOLOGO','RECEPCIONISTA')")
    public ResponseEntity<PageResponse<PatientResponse>> getAll(
            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de registros por página", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Campo de ordenamiento", example = "apellidos")
            @RequestParam(defaultValue = "id") String sort,

            @Parameter(description = "Dirección del orden: asc | desc", example = "asc")
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable  = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(patientService.findAll(pageable));
    }

    // ─── GET por ID ───────────────────────────────────────────────────────────

    @Operation(summary = "Obtener paciente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ODONTOLOGO','RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Operation(summary = "Crear paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente creado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario asociado no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(request));
    }

    // ─── PUT ──────────────────────────────────────────────────────────────────

    @Operation(summary = "Actualizar paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente actualizado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request
    ) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Operation(summary = "Eliminar (desactivar) paciente", description = "Baja lógica — cambia estado a INACTIVO")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente desactivado"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
