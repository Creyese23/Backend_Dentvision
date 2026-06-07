package co.edu.sena.Dentvision_Backend.mapper;

import co.edu.sena.Dentvision_Backend.dto.user.UserRequest;
import co.edu.sena.Dentvision_Backend.dto.user.UserResponse;
import co.edu.sena.Dentvision_Backend.entity.User;
import org.mapstruct.*;

/**
 * Mapper User ↔ DTO.
 * La contraseña nunca se incluye en la respuesta.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "password",           ignore = true)   // el servicio encodea la contraseña
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fechaEliminacion",   ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "password",           ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fechaEliminacion",   ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User user);
}
