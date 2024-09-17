package com.gestaocerta.microempregados.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaocerta.microempregados.dtos.EmpregadoDTO;
import com.gestaocerta.microempregados.controllers.EmpregadoController;
import com.gestaocerta.microempregados.services.EmpregadoService;
import com.gestaocerta.microempregados.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpregadoController.class)
public class EmpregadoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpregadoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private EmpregadoDTO empregadoDTO;

    @BeforeEach
    void setUp() {
        empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setId(1L);
        empregadoDTO.setNome("Empregado Teste");
    }

    @Test
    public void getAllEmpregadosShouldReturnListOfDTOs() throws Exception {
        when(service.getAll()).thenReturn(List.of(empregadoDTO));

        mockMvc.perform(get("/empregados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(empregadoDTO.getId()))
                .andExpect(jsonPath("$[0].nome").value(empregadoDTO.getNome()));

        verify(service, times(1)).getAll();
    }

    @Test
    public void getEmpregadoByIdShouldReturnDTOWhenIdExists() throws Exception {
        when(service.findById(1L)).thenReturn(empregadoDTO);

        mockMvc.perform(get("/empregados/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(empregadoDTO.getId()))
                .andExpect(jsonPath("$.nome").value(empregadoDTO.getNome()));

        verify(service, times(1)).findById(1L);
    }

    @Test
    public void getEmpregadoByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.findById(1L)).thenThrow(new ResourceNotFoundException("Empregado não encontrado"));

        mockMvc.perform(get("/empregados/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findById(1L);
    }

    @Test
    public void createEmpregadoShouldReturnCreatedStatus() throws Exception {
        when(service.insert(any(EmpregadoDTO.class))).thenReturn(empregadoDTO);

        mockMvc.perform(post("/empregados")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empregadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(empregadoDTO.getId()))
                .andExpect(jsonPath("$.nome").value(empregadoDTO.getNome()));

        verify(service, times(1)).insert(any(EmpregadoDTO.class));
    }

    @Test
    public void updateEmpregadoShouldReturnUpdatedDTO() throws Exception {
        when(service.update(eq(1L), any(EmpregadoDTO.class))).thenReturn(empregadoDTO);

        mockMvc.perform(put("/empregados/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empregadoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(empregadoDTO.getId()))
                .andExpect(jsonPath("$.nome").value(empregadoDTO.getNome()));

        verify(service, times(1)).update(eq(1L), any(EmpregadoDTO.class));
    }

    @Test
    public void deleteEmpregadoShouldReturnNoContentWhenSuccessful() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/empregados/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    public void deleteEmpregadoShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Empregado não encontrado")).when(service).deleteById(1L);

        mockMvc.perform(delete("/empregados/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteById(1L);
    }

}
