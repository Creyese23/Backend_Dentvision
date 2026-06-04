package co.edu.sena.Dentvision_Backend.dto.supply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stockMinimo;
    private String unidadMedida;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
