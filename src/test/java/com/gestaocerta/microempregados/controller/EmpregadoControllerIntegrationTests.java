package com.gestaocerta.microempregados.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaocerta.microempregados.dtos.EmpregadoDTO;
import com.gestaocerta.microempregados.repositories.EmpregadoRepository;
import com.gestaocerta.microempregados.entities.Empregado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.rabbitmq.listener.simple.auto-startup=false",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
@Transactional
public class EmpregadoControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpregadoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll();

        Empregado empregado1 = new Empregado();
        empregado1.setNome("Empregado Teste 1");
        repository.save(empregado1);

        Empregado empregado2 = new Empregado();
        empregado2.setNome("Empregado Teste 2");
        repository.save(empregado2);

        existingId = empregado1.getId();
        nonExistingId = 1000L;
    }

    @Test
    public void getAllShouldReturnAllEmpregados() throws Exception {
        mockMvc.perform(get("/empregados")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingId))
                .andExpect(jsonPath("$[0].nome").value("Empregado Teste 1"));
    }

    @Test
    public void findByIdShouldReturnEmpregadoWhenIdExists() throws Exception {
        mockMvc.perform(get("/empregados/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.nome").value("Empregado Teste 1"));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/empregados/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldCreateEmpregado() throws Exception {
        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Novo Empregado");

        String json = objectMapper.writeValueAsString(empregadoDTO);

        mockMvc.perform(post("/empregados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Novo Empregado"));
    }

    @Test
    public void updateShouldUpdateEmpregadoWhenIdExists() throws Exception {
        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Empregado Atualizado");

        String json = objectMapper.writeValueAsString(empregadoDTO);

        mockMvc.perform(put("/empregados/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.nome").value("Empregado Atualizado"));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Empregado Atualizado");

        String json = objectMapper.writeValueAsString(empregadoDTO);

        mockMvc.perform(put("/empregados/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldRemoveEmpregadoWhenIdExists() throws Exception {
        mockMvc.perform(delete("/empregados/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        boolean exists = repository.existsById(existingId);
        assertThat(exists).isFalse();
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/empregados/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}