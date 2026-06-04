package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.order.OrderRequest;
import co.edu.sena.Dentvision_Backend.dto.order.OrderResponse;
import co.edu.sena.Dentvision_Backend.entity.OrderEntity;
import co.edu.sena.Dentvision_Backend.entity.ProcedureEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.OrderRepository;
import co.edu.sena.Dentvision_Backend.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProcedureRepository procedureRepository;

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + id));
        return mapToResponse(order);
    }

    public OrderResponse create(OrderRequest request) {
        ProcedureEntity procedure = procedureRepository.findById(request.getIdProcedimiento())
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + request.getIdProcedimiento()));

        OrderEntity order = OrderEntity.builder()
                .fechaCreacion(request.getFechaCreacion())
                .descripcion(request.getDescripcion())
                .estado(request.getEstado())
                .procedimiento(procedure)
                .build();

        return mapToResponse(orderRepository.save(order));
    }

    public OrderResponse update(Long id, OrderRequest request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + id));

        ProcedureEntity procedure = procedureRepository.findById(request.getIdProcedimiento())
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + request.getIdProcedimiento()));

        order.setFechaCreacion(request.getFechaCreacion());
        order.setDescripcion(request.getDescripcion());
        order.setEstado(request.getEstado());
        order.setProcedimiento(procedure);

        return mapToResponse(orderRepository.save(order));
    }

    public void delete(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + id));
        orderRepository.delete(order);
    }

    private OrderResponse mapToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .fechaCreacion(order.getFechaCreacion())
                .descripcion(order.getDescripcion())
                .estado(order.getEstado())
                .idProcedimiento(order.getProcedimiento().getId())
                .build();
    }
}
