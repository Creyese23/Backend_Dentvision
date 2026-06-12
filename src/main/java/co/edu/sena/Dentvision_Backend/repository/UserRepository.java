package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentificacion(String identificacion);

    Optional<User> findByEmail(String email);

    Optional<User> findByNombres(String nombres);

    Optional<User> findByApellidos(String apellidos);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.estado = 'ACTIVO'")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.estado = 'ACTIVO'")
    Optional<User> findActiveById(Long id);
}

