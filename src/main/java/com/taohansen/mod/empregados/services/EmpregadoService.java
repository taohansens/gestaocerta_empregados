package com.taohansen.mod.empregados.services;

import com.taohansen.mod.empregados.dtos.EmpregadoDTO;
import com.taohansen.mod.empregados.entities.Empregado;
import com.taohansen.mod.empregados.mappers.EmpregadoMapper;
import com.taohansen.mod.empregados.repositories.EmpregadoRepository;
import com.taohansen.mod.empregados.services.exceptions.DatabaseException;
import com.taohansen.mod.empregados.services.exceptions.ResourceNotFoundException;
import com.taohansen.mod.empregados.services.impl.EmpregadoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpregadoService implements EmpregadoServiceImpl {
    private final EmpregadoRepository repository;

    @Autowired
    private EmpregadoMapper empregadoMapper;


    @Transactional(readOnly = true)
    public List<EmpregadoDTO> getAll() {
        List<Empregado> empregados = repository.findAll();
        return empregados.stream()
                .map(empregado -> empregadoMapper.toDto(empregado))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpregadoDTO findById(Long id) {
        Optional<Empregado> obj = repository.findById(id);
        Empregado entity = obj.orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado.", id)));
        return empregadoMapper.toDto(entity);
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
    public EmpregadoDTO insert(EmpregadoDTO dto) {
        Empregado empregado = empregadoMapper.toEntity(dto);
        Empregado entity = repository.save(empregado);
        return empregadoMapper.toDto(entity);
    }

    @Override
    public EmpregadoDTO update(Long id, EmpregadoDTO dto) {
        try {
            Empregado entity = repository.getReferenceById(id);
            empregadoMapper.updateEntityFromDto(dto, entity);
            entity = repository.save(entity);
            return empregadoMapper.toDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        }
    }
}
