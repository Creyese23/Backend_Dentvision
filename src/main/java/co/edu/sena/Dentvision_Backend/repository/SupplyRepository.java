package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    @Query("SELECT s FROM Supply s WHERE s.estado = 'ACTIVO'")
    List<Supply> findAllActive();

    @Query("SELECT s FROM Supply s WHERE s.id = :id AND s.estado = 'ACTIVO'")
    Optional<Supply> findActiveById(Long id);
}

