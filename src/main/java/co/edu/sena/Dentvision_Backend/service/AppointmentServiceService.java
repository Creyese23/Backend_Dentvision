package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.appointmentService.AppointmentServiceRequest;
import co.edu.sena.Dentvision_Backend.dto.appointmentService.AppointmentServiceResponse;
import co.edu.sena.Dentvision_Backend.entity.Appointment;
import co.edu.sena.Dentvision_Backend.entity.AppointmentService;
import co.edu.sena.Dentvision_Backend.entity.ServiceEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.AppointmentRepository;
import co.edu.sena.Dentvision_Backend.repository.AppointmentServiceRepository;
import co.edu.sena.Dentvision_Backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceService {

    private final AppointmentServiceRepository appointmentServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    public List<AppointmentServiceResponse> findAll() {
        return appointmentServiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentServiceResponse findById(Long id) {
        AppointmentService appointmentService = appointmentServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio de cita no encontrado con id " + id));
        return mapToResponse(appointmentService);
    }

    public AppointmentServiceResponse create(AppointmentServiceRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + request.getIdCita()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        AppointmentService appointmentService = AppointmentService.builder()
                .cita(appointment)
                .servicio(service)
                .precioAcordado(request.getPrecioAcordado())
                .build();

        return mapToResponse(appointmentServiceRepository.save(appointmentService));
    }

    public AppointmentServiceResponse update(Long id, AppointmentServiceRequest request) {
        AppointmentService appointmentService = appointmentServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio de cita no encontrado con id " + id));

        Appointment appointment = appointmentRepository.findById(request.getIdCita())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + request.getIdCita()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        appointmentService.setCita(appointment);
        appointmentService.setServicio(service);
        appointmentService.setPrecioAcordado(request.getPrecioAcordado());

        return mapToResponse(appointmentServiceRepository.save(appointmentService));
    }

    public void delete(Long id) {
        AppointmentService appointmentService = appointmentServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio de cita no encontrado con id " + id));
        appointmentServiceRepository.delete(appointmentService);
    }

    private AppointmentServiceResponse mapToResponse(AppointmentService appointmentService) {
        return AppointmentServiceResponse.builder()
                .id(appointmentService.getId())
                .idCita(appointmentService.getCita().getId())
                .idServicio(appointmentService.getServicio().getId())
                .precioAcordado(appointmentService.getPrecioAcordado())
                .build();
    }
}
