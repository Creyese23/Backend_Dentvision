package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para roles clínicos de empleados (tabla `roles`).
 *
 * CORRECCIÓN: RoleService usaba RoleEntityRepository que no existía,
 * causando error de compilación. Se crea aquí.
 */
@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByNombreRol(RoleEntity.NombreRol nombreRol);
    boolean existsByNombreRol(RoleEntity.NombreRol nombreRol);
}
