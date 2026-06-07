package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleRequest;
import co.edu.sena.Dentvision_Backend.dto.employeeRole.EmployeeRoleResponse;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.EmployeeRole;
import co.edu.sena.Dentvision_Backend.entity.RoleEntity;
import co.edu.sena.Dentvision_Backend.exception.DuplicateResourceException;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRoleRepository;
import co.edu.sena.Dentvision_Backend.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NUEVO: servicio faltante que causaba error de compilación en
 * EmployeeRoleController. Gestiona la asignación de roles clínicos
 * a empleados.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeRoleService {

    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleEntityRepository roleEntityRepository;

    public List<EmployeeRoleResponse> findAll() {
        return employeeRoleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeRoleResponse findById(Long id) {
        EmployeeRole er = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asignación de rol no encontrada con id " + id));
        return mapToResponse(er);
    }

    public EmployeeRoleResponse create(EmployeeRoleRequest request) {
        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con id " + request.getIdEmpleado()));

        RoleEntity role = roleEntityRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado con id " + request.getIdRol()));

        if (employeeRoleRepository.existsByEmpleadoIdAndRolId(
                request.getIdEmpleado(), request.getIdRol())) {
            throw new DuplicateResourceException(
                    "El empleado ya tiene asignado ese rol");
        }

        EmployeeRole er = EmployeeRole.builder()
                .empleado(employee)
                .rol(role)
                .build();

        return mapToResponse(employeeRoleRepository.save(er));
    }

    public EmployeeRoleResponse update(Long id, EmployeeRoleRequest request) {
        EmployeeRole er = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asignación de rol no encontrada con id " + id));

        Employee employee = employeeRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con id " + request.getIdEmpleado()));

        RoleEntity role = roleEntityRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado con id " + request.getIdRol()));

        er.setEmpleado(employee);
        er.setRol(role);
        return mapToResponse(employeeRoleRepository.save(er));
    }

    public void delete(Long id) {
        EmployeeRole er = employeeRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asignación de rol no encontrada con id " + id));
        employeeRoleRepository.delete(er);
    }

    private EmployeeRoleResponse mapToResponse(EmployeeRole er) {
        return EmployeeRoleResponse.builder()
                .id(er.getId())
                .idEmpleado(er.getEmpleado().getId())
                .idRol(er.getRol().getId())
                .build();
    }
}
