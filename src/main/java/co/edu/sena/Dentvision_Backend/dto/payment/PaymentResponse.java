package co.edu.sena.Dentvision_Backend.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long idFactura;
    private LocalDate fechaPago;
    private String metodoPago;
    private BigDecimal valor;
    private String estado;
    private LocalDateTime fechaCreacion;
}
