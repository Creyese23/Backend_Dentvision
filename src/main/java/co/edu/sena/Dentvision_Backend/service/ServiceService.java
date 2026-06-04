package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.service.ServiceRequest;
import co.edu.sena.Dentvision_Backend.dto.service.ServiceResponse;
import co.edu.sena.Dentvision_Backend.entity.ServiceEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public List<ServiceResponse> findAll() {
        return serviceRepository.findAllByEstado("ACTIVO")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceResponse findById(Long id) {
        ServiceEntity service = serviceRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Servicio con id " + id + " no encontrado"));
        return toResponse(service);
    }

    @Transactional
    public ServiceResponse create(ServiceRequest request) {
        ServiceEntity service = ServiceEntity.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .duracionEstimada(request.duracionEstimada())
                .estado("ACTIVO")
                .build();

        ServiceEntity saved = serviceRepository.save(service);
        return toResponse(saved);
    }

    @Transactional
    public ServiceResponse update(Long id, ServiceRequest request) {
        ServiceEntity service = serviceRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Servicio con id " + id + " no encontrado"));

        service.setNombre(request.nombre());
        service.setDescripcion(request.descripcion());
        service.setPrecio(request.precio());
        service.setDuracionEstimada(request.duracionEstimada());

        ServiceEntity updated = serviceRepository.save(service);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        ServiceEntity service = serviceRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Servicio con id " + id + " no encontrado"));

        service.setEstado("INACTIVO");
        serviceRepository.save(service);
    }

    private ServiceResponse toResponse(ServiceEntity s) {
        return new ServiceResponse(
                s.getId(),
                s.getNombre(),
                s.getDescripcion(),
                s.getPrecio(),
                s.getDuracionEstimada(),
                s.getEstado(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }
}


