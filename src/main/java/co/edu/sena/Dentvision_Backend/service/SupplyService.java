package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.supply.SupplyRequest;
import co.edu.sena.Dentvision_Backend.dto.supply.SupplyResponse;
import co.edu.sena.Dentvision_Backend.entity.Supply;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplyService {

    private final SupplyRepository supplyRepository;

    public List<SupplyResponse> findAll() {
        return supplyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SupplyResponse findById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + id));
        return mapToResponse(supply);
    }

    public SupplyResponse create(SupplyRequest request) {
        Supply supply = Supply.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .stockMinimo(request.getStockMinimo())
                .unidadMedida(request.getUnidadMedida())
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                .build();

        return mapToResponse(supplyRepository.save(supply));
    }

    public SupplyResponse update(Long id, SupplyRequest request) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + id));

        supply.setNombre(request.getNombre());
        supply.setDescripcion(request.getDescripcion());
        supply.setStockMinimo(request.getStockMinimo());
        supply.setUnidadMedida(request.getUnidadMedida());
        if (request.getEstado() != null) {
            supply.setEstado(request.getEstado());
        }

        return mapToResponse(supplyRepository.save(supply));
    }

    public void delete(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + id));
        supply.setEstado("INACTIVO");
        supplyRepository.save(supply);
    }

    private SupplyResponse mapToResponse(Supply supply) {
        return SupplyResponse.builder()
                .id(supply.getId())
                .nombre(supply.getNombre())
                .descripcion(supply.getDescripcion())
                .stockMinimo(supply.getStockMinimo())
                .unidadMedida(supply.getUnidadMedida())
                .estado(supply.getEstado())
                .fechaCreacion(supply.getFechaCreacion())
                .fechaActualizacion(supply.getFechaActualizacion())
                .build();
    }
}
