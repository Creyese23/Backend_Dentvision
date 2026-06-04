package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.conversation.ConversationRequest;
import co.edu.sena.Dentvision_Backend.dto.conversation.ConversationResponse;
import co.edu.sena.Dentvision_Backend.entity.Conversation;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.entity.ProcedureEntity;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.repository.ConversationRepository;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
import co.edu.sena.Dentvision_Backend.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final PatientRepository patientRepository;
    private final ProcedureRepository procedureRepository;

    public List<ConversationResponse> findAll() {
        return conversationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ConversationResponse findById(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + id));
        return mapToResponse(conversation);
    }

    public ConversationResponse create(ConversationRequest request) {
        Patient patient = patientRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.getIdPaciente()));

        ProcedureEntity procedure = procedureRepository.findById(request.getIdProcedimiento())
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + request.getIdProcedimiento()));

        Conversation conversation = Conversation.builder()
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .paciente(patient)
                .procedimiento(procedure)
                .build();

        return mapToResponse(conversationRepository.save(conversation));
    }

    public ConversationResponse update(Long id, ConversationRequest request) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + id));

        Patient patient = patientRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id " + request.getIdPaciente()));

        ProcedureEntity procedure = procedureRepository.findById(request.getIdProcedimiento())
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id " + request.getIdProcedimiento()));

        conversation.setFechaInicio(request.getFechaInicio());
        conversation.setFechaFin(request.getFechaFin());
        conversation.setPaciente(patient);
        conversation.setProcedimiento(procedure);

        return mapToResponse(conversationRepository.save(conversation));
    }

    public void delete(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + id));
        conversationRepository.delete(conversation);
    }

    private ConversationResponse mapToResponse(Conversation conversation) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .fechaInicio(conversation.getFechaInicio())
                .fechaFin(conversation.getFechaFin())
                .idPaciente(conversation.getPaciente().getId())
                .idProcedimiento(conversation.getProcedimiento().getId())
                .build();
    }
}
