package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.procedure.ProcedureRequest;
import co.edu.sena.Dentvision_Backend.dto.procedure.ProcedureResponse;
import co.edu.sena.Dentvision_Backend.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedimientos")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping
    public ResponseEntity<List<ProcedureResponse>> getAll() {
        return ResponseEntity.ok(procedureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedureResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(procedureService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProcedureResponse> create(@Valid @RequestBody ProcedureRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedureService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedureResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProcedureRequest request
    ) {
        return ResponseEntity.ok(procedureService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        procedureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
