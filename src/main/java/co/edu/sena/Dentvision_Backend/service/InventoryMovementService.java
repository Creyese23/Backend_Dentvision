package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.inventoryMovement.InventoryMovementRequest;
import co.edu.sena.Dentvision_Backend.dto.inventoryMovement.InventoryMovementResponse;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.InventoryMovement;
import co.edu.sena.Dentvision_Backend.entity.Supply;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.InventoryMovementRepository;
import co.edu.sena.Dentvision_Backend.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryMovementService {

    private final InventoryMovementRepository inventoryMovementRepository;
    private final SupplyRepository supplyRepository;
    private final EmployeeRepository employeeRepository;

    public List<InventoryMovementResponse> findAll() {
        return inventoryMovementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InventoryMovementResponse findById(Long id) {
        InventoryMovement movement = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado con id " + id));
        return mapToResponse(movement);
    }

    public InventoryMovementResponse create(InventoryMovementRequest request) {
        Supply supply = supplyRepository.findById(request.getIdInsumo())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + request.getIdInsumo()));

        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdEmpleado()));

        InventoryMovement movement = InventoryMovement.builder()
                .tipoMovimiento(request.getTipoMovimiento())
                .cantidad(request.getCantidad())
                .fecha(request.getFecha())
                .insumo(supply)
                .empleado(employee)
                .build();

        return mapToResponse(inventoryMovementRepository.save(movement));
    }

    public InventoryMovementResponse update(Long id, InventoryMovementRequest request) {
        InventoryMovement movement = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado con id " + id));

        Supply supply = supplyRepository.findById(request.getIdInsumo())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + request.getIdInsumo()));

        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdEmpleado()));

        movement.setTipoMovimiento(request.getTipoMovimiento());
        movement.setCantidad(request.getCantidad());
        movement.setFecha(request.getFecha());
        movement.setInsumo(supply);
        movement.setEmpleado(employee);

        return mapToResponse(inventoryMovementRepository.save(movement));
    }

    public void delete(Long id) {
        InventoryMovement movement = inventoryMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado con id " + id));
        inventoryMovementRepository.delete(movement);
    }

    private InventoryMovementResponse mapToResponse(InventoryMovement movement) {
        return InventoryMovementResponse.builder()
                .id(movement.getId())
                .tipoMovimiento(movement.getTipoMovimiento())
                .cantidad(movement.getCantidad())
                .fecha(movement.getFecha())
                .idInsumo(movement.getInsumo().getId())
                .idEmpleado(movement.getEmpleado().getId())
                .build();
    }
}
