package co.edu.sena.Dentvision_Backend.dto.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {

    @NotNull(message = "La fecha de emisión es requerida")
    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    private String descripcion;
}
