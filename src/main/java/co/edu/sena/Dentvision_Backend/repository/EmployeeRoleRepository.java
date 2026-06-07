package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la tabla `empleado_roles`.
 *
 * CORRECCIÓN: EmployeeRoleController necesitaba un servicio que a su vez
 * necesita este repositorio. Se crea como parte de la corrección del
 * módulo completo EmployeeRole.
 */
@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
    List<EmployeeRole> findByEmpleadoId(Long empleadoId);
    boolean existsByEmpleadoIdAndRolId(Long empleadoId, Long rolId);
}
