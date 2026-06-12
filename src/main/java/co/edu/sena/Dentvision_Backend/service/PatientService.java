package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.common.PageResponse;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.mapper.PatientMapper;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de gestión de pacientes.
 *
 * Cambios respecto a la versión original:
 * 1. Usa {@link PatientMapper} (MapStruct) — elimina el método mapToResponse() manual.
 * 2. {@code findAll(Pageable)} devuelve {@link PageResponse} para paginación.
 * 3. {@code findAll()} sin parámetros sigue disponible para compatibilidad.
 * 4. Logs con SLF4J (@Slf4j) en operaciones clave.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository    userRepository;
    private final PatientMapper     patientMapper;          // inyectado por MapStruct + Spring

    // ─── Consultas ────────────────────────────────────────────────────────────

    /** Lista todos los pacientes activos sin paginar (mantiene compatibilidad). */
    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        log.debug("Consultando todos los pacientes activos");
        return patientRepository.findAllActive()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PatientResponse> findAll(Pageable pageable) {
        log.debug("Consultando pacientes — página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<PatientResponse> page = patientRepository.findAllActive(pageable)
                .map(patientMapper::toResponse);
        return PageResponse.of(page);
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {
        log.debug("Buscando paciente id={}", id);
        return patientMapper.toResponse(findEntityById(id));
    }

    // ─── Escritura ────────────────────────────────────────────────────────────

    public PatientResponse create(PatientRequest request) {
        User user = userRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + request.idUsuario()));

        Patient patient = patientMapper.toEntity(request);
        patient.setUser(user);
        patient.setEstado("ACTIVO");

        Patient saved = patientRepository.save(patient);
        log.info("Paciente creado id={}, documento={}", saved.getId(), saved.getDocumento());
        return patientMapper.toResponse(saved);
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = findEntityById(id);
        User user = userRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + request.idUsuario()));

        patientMapper.updateEntity(request, patient);   // actualización parcial via MapStruct
        patient.setUser(user);

        Patient saved = patientRepository.save(patient);
        log.info("Paciente actualizado id={}", saved.getId());
        return patientMapper.toResponse(saved);
    }

    /** Baja lógica: cambia estado a INACTIVO y registra fecha de eliminación. */
    public void delete(Long id) {
        Patient patient = findEntityById(id);
        patient.setEstado("INACTIVO");
        patient.setFechaEliminacion(LocalDateTime.now());
        patientRepository.save(patient);
        log.info("Paciente desactivado id={}", id);
    }

    // ─── Helpers privados ─────────────────────────────────────────────────────

    private Patient findEntityById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));
    }
}
