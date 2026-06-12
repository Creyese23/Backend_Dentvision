package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.invoice.InvoiceRequest;
import co.edu.sena.Dentvision_Backend.dto.invoice.InvoiceResponse;
import co.edu.sena.Dentvision_Backend.entity.Invoice;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.InvoiceRepository;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
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
    private final PatientRepository patientRepository;

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
        Patient patient = patientRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.getIdPaciente()));

        Invoice invoice = Invoice.builder()
                .paciente(patient)
                .fechaEmision(request.getFechaEmision())
                .fechaVencimiento(request.getFechaVencimiento())
                .estado(request.getEstado())
                .descripcion(request.getDescripcion())
                .build();

        return mapToResponse(invoiceRepository.save(invoice));
    }

    public InvoiceResponse update(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));

        Patient patient = patientRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.getIdPaciente()));

        invoice.setPaciente(patient);
        invoice.setFechaEmision(request.getFechaEmision());
        invoice.setFechaVencimiento(request.getFechaVencimiento());
        invoice.setEstado(request.getEstado());
        invoice.setDescripcion(request.getDescripcion());

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
                .fechaVencimiento(invoice.getFechaVencimiento())
                .estado(invoice.getEstado())
                .descripcion(invoice.getDescripcion())
                .fechaCreacion(invoice.getFechaCreacion())
                .fechaActualizacion(invoice.getFechaActualizacion())
                .build();
    }
}
