package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.role.RoleRequest;
import co.edu.sena.Dentvision_Backend.dto.role.RoleResponse;
import co.edu.sena.Dentvision_Backend.entity.RoleEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleEntityRepository roleRepository;

    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse findById(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + id));
        return mapToResponse(role);
    }

    public RoleResponse create(RoleRequest request) {
        RoleEntity role = RoleEntity.builder()
                .nombreRol(RoleEntity.NombreRol.valueOf(request.getNombreRol()))
                .build();

        return mapToResponse(roleRepository.save(role));
    }

    public RoleResponse update(Long id, RoleRequest request) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + id));

        role.setNombreRol(RoleEntity.NombreRol.valueOf(request.getNombreRol()));

        return mapToResponse(roleRepository.save(role));
    }

    public void delete(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + id));
        roleRepository.delete(role);
    }

    private RoleResponse mapToResponse(RoleEntity role) {
        return RoleResponse.builder()
                .id(role.getId())
                .nombreRol(String.valueOf(role.getNombreRol()))
                .build();
    }
}
