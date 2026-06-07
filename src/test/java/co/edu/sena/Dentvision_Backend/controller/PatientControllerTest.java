package co.edu.sena.Dentvision_Backend.controller;

import co.edu.sena.Dentvision_Backend.dto.common.PageResponse;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientRequest;
import co.edu.sena.Dentvision_Backend.dto.patient.PatientResponse;
import co.edu.sena.Dentvision_Backend.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de capa web para PatientController.
 *
 * @WebMvcTest carga solo el controlador y sus dependencias web,
 * sin levantar todo el contexto de Spring Boot.
 * Los servicios se mockean con @MockBean.
 */
@WebMvcTest(PatientController.class)
@ActiveProfiles("test")
class PatientControllerTest {

    @Autowired MockMvc       mockMvc;
    @Autowired ObjectMapper  objectMapper;

    @MockBean  PatientService patientService;

    private PatientResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = new PatientResponse(
                1L, 1L, "Juan", "Perez", "123456789",
                "300-000-0000", "Calle 1 #2-3",
                LocalDate.of(1990, 5, 15), "ACTIVO",
                null, null, null
        );
    }

    @Test
    @DisplayName("GET /pacientes retorna 200 y PageResponse para usuario autenticado")
    @WithMockUser(roles = "ADMIN")
    void getAll_authenticated_returns200() throws Exception {
        PageResponse<PatientResponse> page = new PageResponse<>(
                List.of(sampleResponse), 0, 20, 1, 1, true
        );
        when(patientService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/pacientes")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nombres").value("Juan"));
    }

    @Test
    @DisplayName("GET /pacientes retorna 401 sin autenticacion")
    void getAll_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /pacientes/{id} retorna 200 con el paciente correcto")
    @WithMockUser(roles = "ADMIN")
    void getById_existing_returns200() throws Exception {
        when(patientService.findById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.documento").value("123456789"));
    }

    @Test
    @DisplayName("POST /pacientes retorna 201 con datos validos")
    @WithMockUser(roles = "ADMIN")
    void create_validRequest_returns201() throws Exception {
        PatientRequest request = new PatientRequest(
                1L, "Juan", "Perez", "123456789",
                "300-000-0000", "Calle 1 #2-3",
                LocalDate.of(1990, 5, 15)
        );
        when(patientService.create(any(PatientRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombres").value("Juan"));
    }

    @Test
    @DisplayName("POST /pacientes retorna 400 con nombres en blanco (validacion)")
    @WithMockUser(roles = "ADMIN")
    void create_blankNombres_returns400() throws Exception {
        PatientRequest badRequest = new PatientRequest(
                1L, "", "Perez", "123456789",
                null, null, LocalDate.of(1990, 5, 15)
        );

        mockMvc.perform(post("/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /pacientes/{id} retorna 204 para ADMIN")
    @WithMockUser(roles = "ADMIN")
    void delete_admin_returns204() throws Exception {
        doNothing().when(patientService).delete(1L);

        mockMvc.perform(delete("/pacientes/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /pacientes/{id} retorna 403 para RECEPCIONISTA")
    @WithMockUser(roles = "RECEPCIONISTA")
    void delete_recepcionista_returns403() throws Exception {
        mockMvc.perform(delete("/pacientes/1").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
