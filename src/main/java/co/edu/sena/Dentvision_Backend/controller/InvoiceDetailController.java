package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.invoiceDetail.InvoiceDetailRequest;
import co.edu.sena.Dentvision_Backend.dto.invoiceDetail.InvoiceDetailResponse;
import co.edu.sena.Dentvision_Backend.service.InvoiceDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles-factura")
@RequiredArgsConstructor
public class InvoiceDetailController {

    private final InvoiceDetailService invoiceDetailService;

    @GetMapping
    public ResponseEntity<List<InvoiceDetailResponse>> getAll() {
        return ResponseEntity.ok(invoiceDetailService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDetailResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceDetailService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceDetailResponse> create(@Valid @RequestBody InvoiceDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDetailService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDetailResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceDetailRequest request
    ) {
        return ResponseEntity.ok(invoiceDetailService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
