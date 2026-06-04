package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.procedure.ProcedureRequest;
import co.edu.sena.Dentvision_Backend.dto.procedure.ProcedureResponse;
import co.edu.sena.Dentvision_Backend.entity.Appointment;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.ProcedureEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.AppointmentRepository;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcedureService {

    private final ProcedureRepository procedureRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<ProcedureResponse> findAll() {
        return procedureRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProcedureResponse findById(Long id) {
        ProcedureEntity procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + id));
        return mapToResponse(procedure);
    }

    public ProcedureResponse create(ProcedureRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + request.getIdCita()));

        Employee employee = employeeRepository.findById(request.getIdTecnico())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdTecnico()));

        ProcedureEntity procedure = ProcedureEntity.builder()
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .observaciones(request.getObservaciones())
                .estado(request.getEstado())
                .cita(appointment)
                .tecnico(employee)
                .build();

        return mapToResponse(procedureRepository.save(procedure));
    }

    public ProcedureResponse update(Long id, ProcedureRequest request) {
        ProcedureEntity procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + id));

        Appointment appointment = appointmentRepository.findById(request.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + request.getIdCita()));

        Employee employee = employeeRepository.findById(request.getIdTecnico())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdTecnico()));

        procedure.setFechaInicio(request.getFechaInicio());
        procedure.setFechaFin(request.getFechaFin());
        procedure.setObservaciones(request.getObservaciones());
        procedure.setEstado(request.getEstado());
        procedure.setCita(appointment);
        procedure.setTecnico(employee);

        return mapToResponse(procedureRepository.save(procedure));
    }

    public void delete(Long id) {
        ProcedureEntity procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + id));
        procedure.setFechaEliminacion(java.time.LocalDateTime.now());
        procedureRepository.save(procedure);
    }

    private ProcedureResponse mapToResponse(ProcedureEntity procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId())
                .fechaInicio(procedure.getFechaInicio())
                .fechaFin(procedure.getFechaFin())
                .observaciones(procedure.getObservaciones())
                .estado(procedure.getEstado())
                .idCita(procedure.getCita().getId())
                .idTecnico(procedure.getTecnico().getId())
                .fechaCreacion(procedure.getFechaCreacion())
                .fechaActualizacion(procedure.getFechaActualizacion())
                .build();
    }
}
