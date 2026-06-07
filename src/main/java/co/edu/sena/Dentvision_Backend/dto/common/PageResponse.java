package co.edu.sena.Dentvision_Backend.dto.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper genérico de respuesta paginada.
 *
 * Uso en servicio:
 * <pre>
 *   Page&lt;Patient&gt; page = patientRepository.findAll(pageable);
 *   return PageResponse.of(page.map(mapper::toResponse));
 * </pre>
 *
 * Respuesta JSON:
 * <pre>
 * {
 *   "content": [...],
 *   "page": 0,
 *   "size": 20,
 *   "totalElements": 150,
 *   "totalPages": 8,
 *   "last": false
 * }
 * </pre>
 */
public record PageResponse<T>(
        List<T>  content,
        int      page,
        int      size,
        long     totalElements,
        int      totalPages,
        boolean  last
) {
    /** Factory method desde un {@link Page} de Spring Data. */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
