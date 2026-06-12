package co.edu.sena.Dentvision_Backend.repository;

import co.edu.sena.Dentvision_Backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.estado <> 'CANCELADA'")
    List<Appointment> findAllActive();

    @Query("SELECT a FROM Appointment a WHERE a.id = :id AND a.estado <> 'CANCELADA'")
    Optional<Appointment> findActiveById(Long id);
}

