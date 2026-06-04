package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.payment.PaymentRequest;
import co.edu.sena.Dentvision_Backend.dto.payment.PaymentResponse;
import co.edu.sena.Dentvision_Backend.entity.Invoice;
import co.edu.sena.Dentvision_Backend.entity.Payment;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.InvoiceRepository;
import co.edu.sena.Dentvision_Backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + id));
        return mapToResponse(payment);
    }

    public PaymentResponse create(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getIdFactura())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + request.getIdFactura()));

        Payment payment = Payment.builder()
                .factura(invoice)
                .fechaPago(request.getFechaPago())
                .metodoPago(request.getMetodoPago())
                .valor(request.getValor())
                .estado(request.getEstado())
                .build();

        return mapToResponse(paymentRepository.save(payment));
    }

    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + id));

        Invoice invoice = invoiceRepository.findById(request.getIdFactura())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + request.getIdFactura()));

        payment.setFactura(invoice);
        payment.setFechaPago(request.getFechaPago());
        payment.setMetodoPago(request.getMetodoPago());
        payment.setValor(request.getValor());
        payment.setEstado(request.getEstado());

        return mapToResponse(paymentRepository.save(payment));
    }

    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id " + id));
        paymentRepository.delete(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .idFactura(payment.getFactura().getId())
                .fechaPago(payment.getFechaPago())
                .metodoPago(payment.getMetodoPago())
                .valor(payment.getValor())
                .estado(payment.getEstado())
                .fechaCreacion(payment.getFechaCreacion())
                .build();
    }
}
