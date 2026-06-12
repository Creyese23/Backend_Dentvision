package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByIdentificacion(String identificacion);

    @Query("SELECT e FROM Employee e WHERE e.estado = 'ACTIVO'")
    List<Employee> findAllActive();

    @Query("SELECT e FROM Employee e WHERE e.id = :id AND e.estado = 'ACTIVO'")
    Optional<Employee> findActiveById(Long id);
}

