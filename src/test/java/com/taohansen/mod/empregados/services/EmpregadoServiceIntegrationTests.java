package com.taohansen.mod.empregados.services;

import com.taohansen.mod.empregados.dtos.EmpregadoDTO;
import com.taohansen.mod.empregados.entities.Empregado;
import com.taohansen.mod.empregados.entities.enums.Sexo;
import com.taohansen.mod.empregados.repositories.EmpregadoRepository;
import com.taohansen.mod.empregados.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.rabbitmq.listener.simple.auto-startup=false",
        "eureka.client.enabled=false"
})
@Transactional
public class EmpregadoServiceIntegrationTests {
    @Autowired
    private EmpregadoService service;

    @Autowired
    private EmpregadoRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalEmpregados;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void getAllShouldReturnAllEmpregados() {
        popularBanco();

        List<EmpregadoDTO> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().anyMatch(e -> e.getNome().equals("Empregado Teste 1"))).isTrue();
        assertThat(result.stream().anyMatch(e -> e.getNome().equals("Empregado Teste 2"))).isTrue();
    }

    @Test
    public void getAllShouldReturnEmptyListWhenNoEmpregados() {
        List<EmpregadoDTO> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void getAllShouldReturnCorrectNumberOfEmpregados() {
        popularBanco();

        List<EmpregadoDTO> result = service.getAll();

        assertThat(result.size()).isEqualTo(countTotalEmpregados.intValue());
    }

    @Test
    public void findByIdShouldReturnEmpregadoWhenIdExists() {
        popularBanco();
        EmpregadoDTO result = service.findById(existingId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        assertThat(result.getNome()).isEqualTo("Empregado Teste 2");
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        popularBanco();

        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdPopuledDbShouldReturnEmpregadoWithCorrectFields() {
        popularBanco();

        Empregado empregado = new Empregado();
        empregado.setNome("Empregado Teste");
        empregado.setCpf("123.456.789-00");
        empregado.setNascimento(LocalDate.of(1990, 1, 1));
        empregado.setSexo(Sexo.MASCULINO);
        empregado = repository.save(empregado);
        long finalCount = repository.count();

        EmpregadoDTO result = service.findById(empregado.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(empregado.getId());
        assertThat(result.getNome()).isEqualTo(empregado.getNome());
        assertThat(result.getCpf()).isEqualTo(empregado.getCpf());
        assertThat(result.getNascimento()).isEqualTo(empregado.getNascimento());
        assertThat(result.getSexo()).isEqualTo(empregado.getSexo());
        assertThat(finalCount).isEqualTo(countTotalEmpregados + 1);
    }

    @Test
    public void deleteShouldRemoveEmpregadoWhenIdExists() {
        popularBanco();

        service.deleteById(existingId);
        long finalCount = repository.count();

        boolean exists = repository.existsById(existingId);

        assertThat(exists).isFalse();
        assertThat(finalCount).isEqualTo(countTotalEmpregados - 1);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        popularBanco();

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });
    }

    @Test
    public void deleteShouldNotModifyDatabaseWhenIdDoesNotExist() {
        popularBanco();

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });

        long finalCount = repository.count();
        assertThat(finalCount).isEqualTo(countTotalEmpregados);
    }

    @Test
    public void insertShouldCreateNewEmpregado() {
        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Novo Empregado");

        EmpregadoDTO result = service.insert(empregadoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNome()).isEqualTo("Novo Empregado");

        boolean exists = repository.existsById(result.getId());
        assertThat(exists).isTrue();

        long finalCount = repository.count();
        assertThat(finalCount).isEqualTo(1);
    }

    @Test
    public void insertShouldCreateNewEmpregadoWhenExistsOtherEmpregados() {
        popularBanco();

        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Novo Empregado");

        EmpregadoDTO result = service.insert(empregadoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNome()).isEqualTo("Novo Empregado");

        boolean exists = repository.existsById(result.getId());
        assertThat(exists).isTrue();

        long finalCount = repository.count();
        assertThat(finalCount).isEqualTo(countTotalEmpregados + 1);
    }

    @Test
    public void insertXTimesShouldCreateNewEmpregados() {
        popularBanco();

        long firstCount = repository.count();
        assertThat(firstCount).isEqualTo(countTotalEmpregados);

        for (int i = 0; i < 5; i++) {
            EmpregadoDTO empregadoDTO = new EmpregadoDTO();
            empregadoDTO.setNome(String.format("Novo Empregado %s", i));
            service.insert(empregadoDTO);
        }

        long finalCount = repository.count();
        assertThat(finalCount).isEqualTo(countTotalEmpregados + 5);
    }

    @Test
    public void insertShouldCreateEmpregadoWithAllFields() {
        EmpregadoDTO empregadoDTO = new EmpregadoDTO();
        empregadoDTO.setNome("Empregado Completo");
        empregadoDTO.setCpf("123.456.789-00");
        empregadoDTO.setNascimento(LocalDate.of(1990, 1, 1));
        empregadoDTO.setSexo(Sexo.MASCULINO);

        EmpregadoDTO result = service.insert(empregadoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNome()).isEqualTo("Empregado Completo");
        assertThat(result.getCpf()).isEqualTo("123.456.789-00");
        assertThat(result.getNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(result.getSexo()).isEqualTo(Sexo.MASCULINO);

        boolean exists = repository.existsById(result.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void updateShouldUpdateEmpregadoWhenIdExists() {
        popularBanco();

        EmpregadoDTO dto = new EmpregadoDTO();
        dto.setNome("Empregado Atualizado");

        EmpregadoDTO result = service.update(existingId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        assertThat(result.getNome()).isEqualTo("Empregado Atualizado");

        Empregado updatedEmpregado = repository.findById(existingId).get();
        assertThat(updatedEmpregado.getNome()).isEqualTo("Empregado Atualizado");
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        popularBanco();

        EmpregadoDTO dto = new EmpregadoDTO();
        dto.setNome("Empregado Atualizado");

        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    @Test
    public void updateShouldNotModifyUnchangedFields() {
        popularBanco();

        EmpregadoDTO dto = new EmpregadoDTO();
        dto.setCpf("123.456.789-00");
        service.update(existingId, dto);

        Empregado updatedEmpregado = repository.findById(existingId).get();
        assertThat(updatedEmpregado.getNome()).isEqualTo("Empregado Teste 2");
        assertThat(updatedEmpregado.getCpf()).isEqualTo("123.456.789-00");
    }

    @Test
    public void updateShouldUpdateAllFields() {
        Empregado empregado = new Empregado();
        empregado.setNome("Empregado Original");
        empregado.setCpf("123.456.789-00");
        empregado.setNascimento(LocalDate.of(1990, 1, 1));
        empregado.setSexo(Sexo.MASCULINO);
        empregado = repository.save(empregado);

        EmpregadoDTO dto = new EmpregadoDTO();
        dto.setNome("Empregado Atualizado");
        dto.setCpf("987.654.321-00");
        dto.setNascimento(LocalDate.of(1985, 5, 5));
        dto.setSexo(Sexo.FEMININO);

        EmpregadoDTO result = service.update(empregado.getId(), dto);
        assertThat(result.getNome()).isEqualTo("Empregado Atualizado");
        assertThat(result.getCpf()).isEqualTo("987.654.321-00");
        assertThat(result.getNascimento()).isEqualTo(LocalDate.of(1985, 5, 5));
        assertThat(result.getSexo()).isEqualTo(Sexo.FEMININO);

        Empregado updatedEmpregado = repository.findById(empregado.getId()).get();
        assertThat(updatedEmpregado.getNome()).isEqualTo("Empregado Atualizado");
        assertThat(updatedEmpregado.getCpf()).isEqualTo("987.654.321-00");
        assertThat(updatedEmpregado.getNascimento()).isEqualTo(LocalDate.of(1985, 5, 5));
        assertThat(updatedEmpregado.getSexo()).isEqualTo(Sexo.FEMININO);
    }


    private void popularBanco() {
        Empregado empregado1 = new Empregado();
        empregado1.setNome("Empregado Teste 1");
        repository.save(empregado1);

        Empregado empregado2 = new Empregado();
        empregado2.setNome("Empregado Teste 2");
        repository.save(empregado2);

        existingId = empregado2.getId();
        nonExistingId = 1000L;
        countTotalEmpregados = repository.count();
    }


}
