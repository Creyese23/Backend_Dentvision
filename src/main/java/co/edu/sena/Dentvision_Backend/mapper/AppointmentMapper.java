package co.edu.sena.Dentvision_Backend.mapper;

import co.edu.sena.Dentvision_Backend.dto.appointment.AppointmentRequest;
import co.edu.sena.Dentvision_Backend.dto.appointment.AppointmentResponse;
import co.edu.sena.Dentvision_Backend.entity.Appointment;
import org.mapstruct.*;

/**
 * Mapper Appointment ↔ DTO.
 *
 * Los campos idPaciente e idOdontologo se mapean desde las relaciones
 * anidadas. Las entidades relacionadas se resuelven en el servicio.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AppointmentMapper {

    @Mapping(source = "paciente.id",   target = "idPaciente")
    @Mapping(source = "odontologo.id", target = "idOdontologo")
    AppointmentResponse toResponse(Appointment appointment);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "paciente",           ignore = true)
    @Mapping(target = "odontologo",         ignore = true)
    @Mapping(target = "estado",             ignore = true)
    @Mapping(target = "fechaCreacion",      ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Appointment toEntity(AppointmentRequest request);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "paciente",           ignore = true)
    @Mapping(target = "odontologo",         ignore = true)
    @Mapping(target = "estado",             ignore = true)
    @Mapping(target = "fechaCreacion",      ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void updateEntity(AppointmentRequest request, @MappingTarget Appointment appointment);
}
