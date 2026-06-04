package co.edu.sena.Dentvision_Backend.dto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer duracionEstimada,
        String estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}


