package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.appointmentService.AppointmentServiceRequest;
import co.edu.sena.Dentvision_Backend.dto.appointmentService.AppointmentServiceResponse;
import co.edu.sena.Dentvision_Backend.service.AppointmentServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios-cita")
@RequiredArgsConstructor
public class AppointmentServiceController {

    private final AppointmentServiceService appointmentServiceService;

    @GetMapping
    public ResponseEntity<List<AppointmentServiceResponse>> getAll() {
        return ResponseEntity.ok(appointmentServiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentServiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentServiceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AppointmentServiceResponse> create(@Valid @RequestBody AppointmentServiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentServiceService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentServiceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentServiceRequest request
    ) {
        return ResponseEntity.ok(appointmentServiceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
