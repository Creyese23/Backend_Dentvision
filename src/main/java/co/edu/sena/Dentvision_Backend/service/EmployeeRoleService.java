package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleRequest;
import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleResponse;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.EmployeeRole;
import co.edu.sena.Dentvision_Backend.entity.RoleEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRoleRepository;
import co.edu.sena.Dentvision_Backend.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeRoleService {

    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleEntityRepository roleRepository;

    public List<EmployeeRoleResponse> findAll() {
        return employeeRoleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeRoleResponse findById(Long id) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol de empleado no encontrado con id " + id));
        return mapToResponse(employeeRole);
    }

    public EmployeeRoleResponse create(EmployeeRoleRequest request) {
        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdEmpleado()));

        RoleEntity role = roleRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + request.getIdRol()));

        EmployeeRole employeeRole = EmployeeRole.builder()
                .empleado(employee)
                .rol(role)
                .build();

        return mapToResponse(employeeRoleRepository.save(employeeRole));
    }

    public EmployeeRoleResponse update(Long id, EmployeeRoleRequest request) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol de empleado no encontrado con id " + id));

        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + request.getIdEmpleado()));

        RoleEntity role = roleRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + request.getIdRol()));

        employeeRole.setEmpleado(employee);
        employeeRole.setRol(role);

        return mapToResponse(employeeRoleRepository.save(employeeRole));
    }

    public void delete(Long id) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol de empleado no encontrado con id " + id));
        employeeRoleRepository.delete(employeeRole);
    }

    private EmployeeRoleResponse mapToResponse(EmployeeRole employeeRole) {
        return EmployeeRoleResponse.builder()
                .id(employeeRole.getId())
                .idEmpleado(employeeRole.getEmpleado().getId())
                .idRol(employeeRole.getRol().getId())
                .build();
    }
}
