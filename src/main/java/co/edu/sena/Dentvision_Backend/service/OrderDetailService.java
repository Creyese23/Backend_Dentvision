package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.orderDetail.OrderDetailRequest;
import co.edu.sena.Dentvision_Backend.dto.orderDetail.OrderDetailResponse;
import co.edu.sena.Dentvision_Backend.entity.OrderDetail;
import co.edu.sena.Dentvision_Backend.entity.OrderEntity;
import co.edu.sena.Dentvision_Backend.entity.ServiceEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.OrderDetailRepository;
import co.edu.sena.Dentvision_Backend.repository.OrderRepository;
import co.edu.sena.Dentvision_Backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;

    public List<OrderDetailResponse> findAll() {
        return orderDetailRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse findById(Long id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de orden no encontrado con id " + id));
        return mapToResponse(detail);
    }

    public OrderDetailResponse create(OrderDetailRequest request) {
        OrderEntity order = orderRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + request.getIdOrden()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        OrderDetail detail = OrderDetail.builder()
                .orden(order)
                .servicio(service)
                .cantidad(request.getCantidad())
                .precioUnitario(request.getPrecioUnitario())
                .observaciones(request.getObservaciones())
                .build();

        return mapToResponse(orderDetailRepository.save(detail));
    }

    public OrderDetailResponse update(Long id, OrderDetailRequest request) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de orden no encontrado con id " + id));

        OrderEntity order = orderRepository.findById(request.getIdOrden())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + request.getIdOrden()));

        ServiceEntity service = serviceRepository.findById(request.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id " + request.getIdServicio()));

        detail.setOrden(order);
        detail.setServicio(service);
        detail.setCantidad(request.getCantidad());
        detail.setPrecioUnitario(request.getPrecioUnitario());
        detail.setObservaciones(request.getObservaciones());

        return mapToResponse(orderDetailRepository.save(detail));
    }

    public void delete(Long id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de orden no encontrado con id " + id));
        orderDetailRepository.delete(detail);
    }

    private OrderDetailResponse mapToResponse(OrderDetail detail) {
        return OrderDetailResponse.builder()
                .id(detail.getId())
                .idOrden(detail.getOrden().getId())
                .idServicio(detail.getServicio().getId())
                .cantidad(detail.getCantidad())
                .precioUnitario(detail.getPrecioUnitario())
                .observaciones(detail.getObservaciones())
                .build();
    }
}
