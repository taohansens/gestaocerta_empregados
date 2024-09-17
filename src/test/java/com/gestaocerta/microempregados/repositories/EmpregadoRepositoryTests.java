package com.gestaocerta.microempregados.repositories;

import com.gestaocerta.microempregados.entities.Empregado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class EmpregadoRepositoryTests {
    @Autowired
    private EmpregadoRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalEmpregados;

    @BeforeEach
    void setUp() throws Exception {
        Empregado emp1 = new Empregado();
        emp1.setNome("Empregado 1");
        emp1.setCpf("98125513019");
        repository.save(emp1);

        Empregado emp2 = new Empregado();
        emp2.setNome("Empregado 2");
        emp2.setCpf("65353747003");
        repository.save(emp2);

        Empregado emp3 = new Empregado();
        emp3.setNome("Empregado 3");
        emp3.setCpf("40456027041");
        repository.save(emp3);

        existingId = emp1.getId();
        nonExistingId = 1000L;
        countTotalEmpregados = repository.count();
    }

    @Test
    public void countShouldReturnCorrectNumberOfEmpregados() {
        assertThat(repository.count()).isEqualTo(countTotalEmpregados);
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        Optional<Empregado> result = repository.findById(existingId);
        assertThat(result).isPresent();
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<Empregado> result = repository.findById(nonExistingId);
        assertThat(result).isEmpty();
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        assertThat(repository.existsById(existingId)).isTrue();
        repository.deleteById(existingId);

        Optional<Empregado> result = repository.findById(existingId);
        assertThat(result).isEmpty();
    }

    @Test
    public void deleteShouldNotThrowExceptionWhenIdDoesNotExist() {
        assertThat(repository.existsById(nonExistingId)).isFalse();
        repository.deleteById(nonExistingId);
        assertThat(repository.count()).isEqualTo(countTotalEmpregados);
    }
}