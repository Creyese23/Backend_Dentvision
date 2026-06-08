package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentificacion(String identificacion);

    Optional<User> findByEmail(String email);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByEmail(String email);

}

