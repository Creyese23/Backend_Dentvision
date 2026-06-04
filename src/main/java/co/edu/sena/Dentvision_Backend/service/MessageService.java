package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.message.MessageRequest;
import co.edu.sena.Dentvision_Backend.dto.message.MessageResponse;
import co.edu.sena.Dentvision_Backend.entity.Message;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageResponse> findAll() {
        return messageRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MessageResponse findById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id " + id));
        return mapToResponse(message);
    }

    public MessageResponse create(MessageRequest request) {
        Message message = Message.builder()
                .contenido(request.getContenido())
                .remitente(request.getRemitente())
                .build();

        return mapToResponse(messageRepository.save(message));
    }

    public MessageResponse update(Long id, MessageRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id " + id));

        message.setContenido(request.getContenido());
        message.setRemitente(request.getRemitente());

        return mapToResponse(messageRepository.save(message));
    }

    public void delete(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con id " + id));
        messageRepository.delete(message);
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .contenido(message.getContenido())
                .remitente(message.getRemitente())
                .build();
    }
}
