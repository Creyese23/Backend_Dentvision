package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.ProcedureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureRepository extends JpaRepository<ProcedureEntity, Long> {

    @Query("SELECT p FROM ProcedureEntity p WHERE p.fechaEliminacion IS NULL")
    List<ProcedureEntity> findAllActive();

    @Query("SELECT p FROM ProcedureEntity p WHERE p.id = :id AND p.fechaEliminacion IS NULL")
    Optional<ProcedureEntity> findActiveById(Long id);
}

