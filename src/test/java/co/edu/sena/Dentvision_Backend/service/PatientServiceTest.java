package co.edu.sena.Dentvision_Backend.service;

import co.edu.sena.Dentvision_Backend.dto.common.PageResponse;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.entity.Patient;
import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.exception.ResourceNotFoundException;
import co.edu.sena.Dentvision_Backend.mapper.PatientMapper;
import co.edu.sena.Dentvision_Backend.repository.PatientRepository;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PatientService.
 * Se mockean repositorios y mapper para aislar la logica del servicio.
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepository patientRepository;
    @Mock private UserRepository    userRepository;
    @Mock private PatientMapper     patientMapper;

    @InjectMocks
    private PatientService patientService;

    private User    sampleUser;
    private Patient samplePatient;
    private PatientResponse sampleResponse;
    private PatientRequest  sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);

        samplePatient = Patient.builder()
                .id(1L)
                .user(sampleUser)
                .nombres("Juan")
                .apellidos("Perez")
                .documento("123456789")
                .estado("ACTIVO")
                .build();

        sampleResponse = new PatientResponse(
                1L, 1L, "Juan", "Perez", "123456789",
                null, null, LocalDate.of(1990, 1, 1),
                "ACTIVO", null, null, null
        );

        sampleRequest = new PatientRequest(
                1L, "Juan", "Perez", "123456789",
                null, null, LocalDate.of(1990, 1, 1)
        );
    }

    @Test
    @DisplayName("findAll(Pageable) devuelve PageResponse con los datos correctos")
    void findAll_pageable_returnsPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(List.of(samplePatient), pageable, 1);

        when(patientRepository.findAll(pageable)).thenReturn(page);
        when(patientMapper.toResponse(samplePatient)).thenReturn(sampleResponse);

        PageResponse<PatientResponse> result = patientService.findAll(pageable);

        assertThat(result.content()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.content().get(0).nombres()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("findById retorna PatientResponse cuando el paciente existe")
    void findById_existingId_returnsResponse() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(patientMapper.toResponse(samplePatient)).thenReturn(sampleResponse);

        PatientResponse result = patientService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.nombres()).isEqualTo("Juan");
    }

    @Test
    @DisplayName("findById lanza ResourceNotFoundException cuando el ID no existe")
    void findById_nonExistingId_throwsException() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("create guarda el paciente y retorna respuesta")
    void create_validRequest_savesAndReturnsResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(patientMapper.toEntity(sampleRequest)).thenReturn(samplePatient);
        when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);
        when(patientMapper.toResponse(samplePatient)).thenReturn(sampleResponse);

        PatientResponse result = patientService.create(sampleRequest);

        assertThat(result).isNotNull();
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("create lanza excepcion si el usuario asociado no existe")
    void create_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.create(sampleRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario");
    }

    @Test
    @DisplayName("delete hace baja logica: estado INACTIVO sin eliminar el registro")
    void delete_existingId_setsInactive() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);

        patientService.delete(1L);

        assertThat(samplePatient.getEstado()).isEqualTo("INACTIVO");
        assertThat(samplePatient.getFechaEliminacion()).isNotNull();
        verify(patientRepository, never()).deleteById(anyLong());
    }
}
