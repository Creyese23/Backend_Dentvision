package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.invoice.InvoiceRequest;
import co.edu.sena.Dentvision_Backend.dto.invoice.InvoiceResponse;
import co.edu.sena.Dentvision_Backend.entity.Invoice;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceResponse> findAll() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        return mapToResponse(invoice);
    }

    public InvoiceResponse create(InvoiceRequest request) {
        Invoice invoice = Invoice.builder()
                .fechaEmision(request.getFechaEmision())
                .build();

        return mapToResponse(invoiceRepository.save(invoice));
    }

    public InvoiceResponse update(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));

        invoice.setFechaEmision(request.getFechaEmision());
        // estado/descripcion mapping intentionally omitted (no matching builder/mutator present)

        return mapToResponse(invoiceRepository.save(invoice));
    }

    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
        invoiceRepository.delete(invoice);
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .fechaEmision(invoice.getFechaEmision())
                // estado/descripcion not mapped: corresponding accessors not available on Invoice
                .fechaCreacion(invoice.getFechaCreacion())
                .fechaActualizacion(invoice.getFechaActualizacion())
                .build();
    }
}
