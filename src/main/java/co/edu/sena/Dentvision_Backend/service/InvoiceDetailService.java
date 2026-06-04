package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.invoiceDetail.InvoiceDetailRequest;
import co.edu.sena.Dentvision_Backend.dto.invoiceDetail.InvoiceDetailResponse;
import co.edu.sena.Dentvision_Backend.entity.Invoice;
import co.edu.sena.Dentvision_Backend.entity.InvoiceDetail;
import co.edu.sena.Dentvision_Backend.entity.ServiceEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.InvoiceDetailRepository;
import co.edu.sena.Dentvision_Backend.repository.InvoiceRepository;
import co.edu.sena.Dentvision_Backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceDetailService {

    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final ServiceRepository serviceRepository;

    public List<InvoiceDetailResponse> findAll() {
        return invoiceDetailRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InvoiceDetailResponse findById(Long id) {
        InvoiceDetail detail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de factura no encontrado con id " + id));
        return mapToResponse(detail);
    }

    public InvoiceDetailResponse create(InvoiceDetailRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getIdFactura())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + request.getIdFactura()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        InvoiceDetail detail = InvoiceDetail.builder()
                .factura(invoice)
                .servicio(service)
                .cantidad(request.getCantidad())
                .precioUnitario(request.getPrecioUnitario())
                .subtotal(request.getSubtotal())
                .build();

        return mapToResponse(invoiceDetailRepository.save(detail));
    }

    public InvoiceDetailResponse update(Long id, InvoiceDetailRequest request) {
        InvoiceDetail detail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de factura no encontrado con id " + id));

        Invoice invoice = invoiceRepository.findById(request.getIdFactura())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + request.getIdFactura()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        detail.setFactura(invoice);
        detail.setServicio(service);
        detail.setCantidad(request.getCantidad());
        detail.setPrecioUnitario(request.getPrecioUnitario());
        detail.setSubtotal(request.getSubtotal());

        return mapToResponse(invoiceDetailRepository.save(detail));
    }

    public void delete(Long id) {
        InvoiceDetail detail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de factura no encontrado con id " + id));
        invoiceDetailRepository.delete(detail);
    }

    private InvoiceDetailResponse mapToResponse(InvoiceDetail detail) {
        return InvoiceDetailResponse.builder()
                .id(detail.getId())
                .idFactura(detail.getFactura().getId())
                .idServicio(detail.getServicio().getId())
                .cantidad(detail.getCantidad())
                .precioUnitario(detail.getPrecioUnitario())
                .subtotal(detail.getSubtotal())
                .build();
    }
}
