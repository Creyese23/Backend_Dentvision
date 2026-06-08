package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.employee.EmployeeRequest;
import co.edu.sena.Dentvision_Backend.dto.employee.EmployeeResponse;
import co.edu.sena.Dentvision_Backend.entity.Employee;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.DuplicateResourceException;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.EmployeeRepository;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con id " + id));
        return mapToResponse(employee);
    }

    /**
     * CORRECCIÓN: el método original no asignaba el User al Employee,
     * causando violación de constraint NOT NULL en la columna id_usuario.
     * Ahora se resuelve el usuario desde el id incluido en el request.
     */
    public EmployeeResponse create(EmployeeRequest request) {
        User user = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id " + request.getIdUsuario()));

        if (employeeRepository.findByUserUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException(
                    "El usuario ya tiene un perfil de empleado asignado");
        }

        Employee employee = Employee.builder()
                .user(user)
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .documento(request.getDocumento())
                .telefono(request.getTelefono())
                .especialidad(request.getEspecialidad())
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con id " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con id " + id));
        employee.setEstado("INACTIVO");
        employeeRepository.save(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .idUsuario(employee.getUser() != null ? employee.getUser().getId() : null)
                .nombres(employee.getNombres())
                .apellidos(employee.getApellidos())
                .documento(employee.getDocumento())
                .telefono(employee.getTelefono())
                .estado(employee.getEstado())
                .build();
    }
}
