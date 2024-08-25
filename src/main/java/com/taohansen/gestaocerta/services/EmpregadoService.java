package com.taohansen.gestaocerta.services;

import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.repositories.EmpregadoRepository;
import com.taohansen.gestaocerta.services.exceptions.DatabaseException;
import com.taohansen.gestaocerta.services.exceptions.ResourceNotFoundException;
import com.taohansen.gestaocerta.services.impl.EmpregadoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpregadoService implements EmpregadoServiceImpl {
    private final EmpregadoRepository repository;

    @Transactional(readOnly = true)
    public List<Empregado> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Empregado findById(Long id) {
        Optional<Empregado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado.", id)));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        } try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Database Integrity Violation");
        }
    }

    @Override
    public Empregado insert(Empregado empregado) {
        Empregado entity = new Empregado();
        BeanUtils.copyProperties(empregado, entity, "id");
        entity = repository.save(entity);
        return entity;
    }

    @Override
    public Empregado update(Long id, Empregado empregado) {
        try {
            Empregado entity = repository.getReferenceById(id);
            BeanUtils.copyProperties(empregado, entity, "id");
            entity = repository.save(entity);
            return entity;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        }
    }

}
