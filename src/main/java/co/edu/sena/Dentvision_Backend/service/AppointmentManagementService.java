package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.appointment.AppointmentRequest;
import co.edu.sena.Dentvision_Backend.dto.appointment.AppointmentResponse;
import co.edu.sena.Dentvision_Backend.entity.Appointment;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.AppointmentRepository;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentManagementService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));
        return mapToResponse(appointment);
    }

    public AppointmentResponse create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.idPaciente()));
        Employee odontologo = employeeRepository.findById(request.idOdontologo())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id " + request.idOdontologo()));

        Appointment appointment = Appointment.builder()
                .paciente(patient)
                .odontologo(odontologo)
                .fechaHora(request.fechaHora())
                .motivo(request.motivo())
                .estado("PENDIENTE")
                .build();

        return mapToResponse(appointmentRepository.save(appointment));
    }

    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));

        Patient patient = patientRepository.findById(request.idPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.idPaciente()));
        Employee odontologo = employeeRepository.findById(request.idOdontologo())
                .orElseThrow(() -> new ResourceNotFoundException("Odontólogo no encontrado con id " + request.idOdontologo()));

        appointment.setPaciente(patient);
        appointment.setOdontologo(odontologo);
        appointment.setFechaHora(request.fechaHora());
        appointment.setMotivo(request.motivo());

        return mapToResponse(appointmentRepository.save(appointment));
    }

    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));
        appointment.setEstado("CANCELADA");
        appointmentRepository.save(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPaciente() != null ? appointment.getPaciente().getId() : null,
                appointment.getOdontologo() != null ? appointment.getOdontologo().getId() : null,
                appointment.getFechaHora(),
                appointment.getMotivo(),
                appointment.getEstado(),
                appointment.getFechaCreacion(),
                appointment.getFechaActualizacion(),
                appointment.getFechaEliminacion()
        );
    }
}
