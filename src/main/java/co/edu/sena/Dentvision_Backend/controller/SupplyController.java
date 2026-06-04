package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.supply.SupplyRequest;
import co.edu.sena.Dentvision_Backend.dto.supply.SupplyResponse;
import co.edu.sena.Dentvision_Backend.service.SupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insumos")
@RequiredArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @GetMapping
    public ResponseEntity<List<SupplyResponse>> getAll() {
        return ResponseEntity.ok(supplyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SupplyResponse> create(@Valid @RequestBody SupplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplyService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplyRequest request
    ) {
        return ResponseEntity.ok(supplyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
