package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.delivery.DeliveryRequest;
import co.edu.sena.Dentvision_Backend.dto.delivery.DeliveryResponse;
import co.edu.sena.Dentvision_Backend.entity.Delivery;
import co.edu.sena.Dentvision_Backend.entity.OrderEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.DeliveryRepository;
import co.edu.sena.Dentvision_Backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    public List<DeliveryResponse> findAll() {
        return deliveryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DeliveryResponse findById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con id " + id));
        return mapToResponse(delivery);
    }

    public DeliveryResponse create(DeliveryRequest request) {
        OrderEntity order = orderRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + request.getIdOrden()));

        Delivery delivery = Delivery.builder()
                .fechaEntrega(request.getFechaEntrega())
                .estado(request.getEstado())
                .observaciones(request.getObservaciones())
                .orden(order)
                .build();

        return mapToResponse(deliveryRepository.save(delivery));
    }

    public DeliveryResponse update(Long id, DeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con id " + id));

        OrderEntity order = orderRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + request.getIdOrden()));

        delivery.setFechaEntrega(request.getFechaEntrega());
        delivery.setEstado(request.getEstado());
        delivery.setObservaciones(request.getObservaciones());
        delivery.setOrden(order);

        return mapToResponse(deliveryRepository.save(delivery));
    }

    public void delete(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con id " + id));
        deliveryRepository.delete(delivery);
    }

    private DeliveryResponse mapToResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .fechaEntrega(delivery.getFechaEntrega())
                .estado(delivery.getEstado())
                .observaciones(delivery.getObservaciones())
                .idOrden(delivery.getOrden().getId())
                .build();
    }
}
