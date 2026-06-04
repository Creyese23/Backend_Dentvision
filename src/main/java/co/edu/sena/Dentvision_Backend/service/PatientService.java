package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));
        return mapToResponse(patient);
    }

    public PatientResponse create(PatientRequest request) {
        User user = userRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + request.idUsuario()));

        Patient patient = Patient.builder()
                .user(user)
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .documento(request.documento())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .fechaNacimiento(request.fechaNacimiento())
                .estado("ACTIVO")
                .build();

        return mapToResponse(patientRepository.save(patient));
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));

        User user = userRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + request.idUsuario()));

        patient.setUser(user);
        patient.setNombres(request.nombres());
        patient.setApellidos(request.apellidos());
        patient.setDocumento(request.documento());
        patient.setTelefono(request.telefono());
        patient.setDireccion(request.direccion());
        patient.setFechaNacimiento(request.fechaNacimiento());

        return mapToResponse(patientRepository.save(patient));
    }

    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + id));
        patient.setEstado("INACTIVO");
        patientRepository.save(patient);
    }

    private PatientResponse mapToResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getUser() != null ? patient.getUser().getId() : null,
                patient.getNombres(),
                patient.getApellidos(),
                patient.getDocumento(),
                patient.getTelefono(),
                patient.getDireccion(),
                patient.getFechaNacimiento(),
                patient.getEstado(),
                patient.getFechaCreacion(),
                patient.getFechaActualizacion(),
                patient.getFechaEliminacion()
        );
    }
}
