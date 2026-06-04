package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.conversation.ConversationRequest;
import co.edu.sena.Dentvision_Backend.dto.conversation.ConversationResponse;
import co.edu.sena.Dentvision_Backend.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversaciones")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getAll() {
        return ResponseEntity.ok(conversationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> create(@Valid @RequestBody ConversationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conversationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ConversationRequest request
    ) {
        return ResponseEntity.ok(conversationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conversationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
