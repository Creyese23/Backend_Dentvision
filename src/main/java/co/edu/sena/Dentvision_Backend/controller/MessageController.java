package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.message.MessageRequest;
import co.edu.sena.Dentvision_Backend.dto.message.MessageResponse;
import co.edu.sena.Dentvision_Backend.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensajes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ODONTOLOGO','AUXILIAR_ADMINISTRATIVA')")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAll() {
        return ResponseEntity.ok(messageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request
    ) {
        return ResponseEntity.ok(messageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
