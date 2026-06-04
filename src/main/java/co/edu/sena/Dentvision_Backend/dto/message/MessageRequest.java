package co.edu.sena.Dentvision_Backend.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotBlank(message = "El contenido es requerido")
    @Size(min = 1, max = 1000, message = "El contenido debe tener entre 1 y 1000 caracteres")
    private String contenido;

    @NotBlank(message = "El remitente es requerido")
    @Size(min = 1, max = 100, message = "El remitente debe tener entre 1 y 100 caracteres")
    private String remitente;

    private String destinatario;
}
