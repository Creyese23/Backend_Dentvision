package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.orderDetail.OrderDetailRequest;
import co.edu.sena.Dentvision_Backend.dto.orderDetail.OrderDetailResponse;
import co.edu.sena.Dentvision_Backend.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles-orden")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> getAll() {
        return ResponseEntity.ok(orderDetailService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderDetailService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> create(@Valid @RequestBody OrderDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderDetailRequest request
    ) {
        return ResponseEntity.ok(orderDetailService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
