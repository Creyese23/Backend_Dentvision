package co.edu.sena.Dentvision_Backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String contenido;
    private String remitente;
    private String destinatario;
    private LocalDateTime fechaCreacion;
}
