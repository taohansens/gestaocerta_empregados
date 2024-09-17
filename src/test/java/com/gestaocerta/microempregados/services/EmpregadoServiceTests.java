package com.gestaocerta.microempregados.services;

import com.gestaocerta.microempregados.dtos.EmpregadoDTO;
import com.gestaocerta.microempregados.mappers.EmpregadoMapper;
import com.gestaocerta.microempregados.repositories.EmpregadoRepository;
import com.gestaocerta.microempregados.entities.Empregado;
import com.gestaocerta.microempregados.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpregadoServiceTests {

    @InjectMocks
    private EmpregadoService service;

    @Mock
    private EmpregadoRepository repository;

    @Mock
    private EmpregadoMapper empregadoMapper;

    private Long existingId;
    private Long nonExistingId;
    private Empregado empregado1;
    private Empregado empregado2;
    private EmpregadoDTO empregadoDTO1;
    private EmpregadoDTO empregadoDTO2;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        empregado1 = new Empregado();
        empregado1.setId(1L);
        empregado1.setNome("Empregado 1");

        empregado2 = new Empregado();
        empregado2.setId(2L);
        empregado2.setNome("Empregado 2");

        empregadoDTO1 = new EmpregadoDTO();
        empregadoDTO1.setId(1L);
        empregadoDTO1.setNome("Empregado 1 DTO");

        empregadoDTO2 = new EmpregadoDTO();
        empregadoDTO2.setId(2L);
        empregadoDTO2.setNome("Empregado 2 DTO");
    }

    @Test
    public void findByIdShouldReturnEmpregadoDTOWhenIdExists() {
        when(repository.findById(existingId)).thenReturn(Optional.of(empregado1));
        when(empregadoMapper.toDto(empregado1)).thenReturn(empregadoDTO1);

        EmpregadoDTO result = service.findById(existingId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        assertThat(result.getNome()).isEqualTo("Empregado 1 DTO");

        verify(repository, times(1)).findById(existingId);
        verify(empregadoMapper, times(1)).toDto(empregado1);
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        verify(repository, times(1)).findById(nonExistingId);
        verify(empregadoMapper, never()).toDto(any());
    }

    @Test
    public void getAllShouldReturnListOfEmpregadoDTOs() {
        when(repository.findAll()).thenReturn(Arrays.asList(empregado1, empregado2));
        when(empregadoMapper.toDto(empregado1)).thenReturn(empregadoDTO1);
        when(empregadoMapper.toDto(empregado2)).thenReturn(empregadoDTO2);

        List<EmpregadoDTO> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(empregadoDTO1, empregadoDTO2);

        verify(repository, times(1)).findAll();
        verify(empregadoMapper, times(1)).toDto(empregado1);
        verify(empregadoMapper, times(1)).toDto(empregado2);
    }

    @Test
    public void updateShouldReturnUpdatedEmpregadoDTOWhenIdExists() {
        when(repository.getReferenceById(existingId)).thenReturn(empregado1);
        doNothing().when(empregadoMapper).updateEntityFromDto(empregadoDTO1, empregado1);

        Empregado updated = new Empregado();
        updated.setId(existingId);
        updated.setNome("Empregado Atualizado");

        EmpregadoDTO updatedDTO = new EmpregadoDTO();
        updatedDTO.setId(existingId);
        updatedDTO.setNome("Empregado Atualizado");

        when(repository.save(empregado1)).thenReturn(updated);

        when(empregadoMapper.toDto(updated)).thenReturn(updatedDTO);

        EmpregadoDTO result = service.update(existingId, empregadoDTO1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        assertThat(result.getNome()).isEqualTo("Empregado Atualizado");

        verify(repository, times(1)).getReferenceById(existingId);
        verify(empregadoMapper, times(1)).updateEntityFromDto(empregadoDTO1, empregado1);
        verify(repository, times(1)).save(empregado1);
        verify(empregadoMapper, times(1)).toDto(updated);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, empregadoDTO1);
        });

        verify(repository, times(1)).getReferenceById(nonExistingId);
        verify(empregadoMapper, never()).updateEntityFromDto(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        when(repository.existsById(existingId)).thenReturn(true);
        doNothing().when(repository).deleteById(existingId);

        service.deleteById(existingId);

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(repository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });

        verify(repository, never()).deleteById(nonExistingId);
    }


}