package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.estado = 'ACTIVO'")
    List<Patient> findAllActive();

    @Query("SELECT p FROM Patient p WHERE p.estado = 'ACTIVO'")
    Page<Patient> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.id = :id AND p.estado = 'ACTIVO'")
    Optional<Patient> findActiveById(Long id);
}

