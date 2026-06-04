package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.employee.EmployeeRequest;
import co.edu.sena.Dentvision_Backend.dto.employee.EmployeeResponse;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + id));
        return mapToResponse(employee);
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = Employee.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .documento(request.getDocumento())
                .telefono(request.getTelefono())
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + id));

        employee.setNombres(request.getNombres());
        employee.setApellidos(request.getApellidos());
        employee.setDocumento(request.getDocumento());
        employee.setTelefono(request.getTelefono());
        if (request.getEstado() != null) {
            employee.setEstado(request.getEstado());
        }

        return mapToResponse(employeeRepository.save(employee));
    }

    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id " + id));
        employee.setEstado("INACTIVO");
        employeeRepository.save(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .nombres(employee.getNombres())
                .apellidos(employee.getApellidos())
                .documento(employee.getDocumento())
                .telefono(employee.getTelefono())
                .estado(employee.getEstado())
                .build();
    }
}
