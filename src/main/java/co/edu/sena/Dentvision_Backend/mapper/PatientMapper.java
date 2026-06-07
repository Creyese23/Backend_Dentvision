package co.edu.sena.Dentvision_Backend.mapper;

import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.entity.User;
import org.mapstruct.*;

/**
 * Mapper automático Patient ↔ DTO usando MapStruct.
 *
 * MapStruct genera en tiempo de compilación una clase PatientMapperImpl
 * que Spring inyecta como bean (gracias a componentModel = "spring").
 *
 * Notas de mapeo:
 * - patient.user.id  →  PatientResponse.idUsuario
 * - PatientRequest no modifica "estado" (se gestiona en el servicio)
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientMapper {

    /** Entidad → DTO de respuesta. */
    @Mapping(source = "user.id", target = "idUsuario")
    PatientResponse toResponse(Patient patient);

    /**
     * DTO de creación → Entidad.
     * Los campos auditados y "estado" se ignoran porque los gestiona
     * JPA Auditing y el servicio respectivamente.
     */
    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "user",              ignore = true)   // se resuelve en el servicio
    @Mapping(target = "estado",            ignore = true)
    @Mapping(target = "fechaCreacion",     ignore = true)
    @Mapping(target = "fechaActualizacion",ignore = true)
    @Mapping(target = "fechaEliminacion",  ignore = true)
    Patient toEntity(PatientRequest request);

    /**
     * Actualización parcial: copia los campos del request sobre una entidad
     * existente, respetando los campos ignorados.
     *
     * Uso: mapper.updateEntity(request, existingPatient);
     */
    @Mapping(target = "id",                ignore = true)
    @Mapping(target = "user",              ignore = true)
    @Mapping(target = "estado",            ignore = true)
    @Mapping(target = "fechaCreacion",     ignore = true)
    @Mapping(target = "fechaActualizacion",ignore = true)
    @Mapping(target = "fechaEliminacion",  ignore = true)
    void updateEntity(PatientRequest request, @MappingTarget Patient patient);
}
