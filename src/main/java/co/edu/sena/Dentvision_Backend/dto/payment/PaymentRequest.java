package co.edu.sena.Dentvision_Backend.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "El ID de la factura es requerido")
    private Long idFactura;

    private LocalDate fechaPago;

    @NotBlank(message = "El método de pago es requerido")
    private String metodoPago;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @NotBlank(message = "El estado es requerido")
    private String estado;
}
