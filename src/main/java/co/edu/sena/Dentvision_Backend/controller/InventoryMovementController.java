package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.inventoryMovement.InventoryMovementRequest;
import co.edu.sena.Dentvision_Backend.dto.inventoryMovement.InventoryMovementResponse;
import co.edu.sena.Dentvision_Backend.service.InventoryMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos-inventario")
@RequiredArgsConstructor
public class InventoryMovementController {

    private final InventoryMovementService inventoryMovementService;

    @GetMapping
    public ResponseEntity<List<InventoryMovementResponse>> getAll() {
        return ResponseEntity.ok(inventoryMovementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryMovementService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InventoryMovementResponse> create(@Valid @RequestBody InventoryMovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryMovementService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryMovementResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InventoryMovementRequest request
    ) {
        return ResponseEntity.ok(inventoryMovementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventoryMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
