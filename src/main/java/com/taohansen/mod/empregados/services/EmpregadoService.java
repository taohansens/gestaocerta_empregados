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
    private final LogProducer log;

    @Autowired
    private EmpregadoMapper empregadoMapper;


    @Transactional(readOnly = true)
    public List<EmpregadoDTO> getAll() {
        log.sendLogMessage("Consulta lista de empregados [GETALL]");
        List<Empregado> empregados = repository.findAll();
        return empregados.stream()
                .map(empregado -> empregadoMapper.toDto(empregado))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpregadoDTO findById(Long id) {
        log.sendLogMessage("Consulta empregado [findById]");
        Optional<Empregado> obj = repository.findById(id);
        Empregado entity = obj.orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado.", id)));
        log.sendAuditMessage("Consulta empregado ID " + id);
        return empregadoMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deleteById(Long id) {
        log.sendLogMessage("Deleta empregado [deleteById]");
        log.sendAuditMessage("Deleta empregado ID " + id);
        if (!repository.existsById(id)) {
            log.sendLogMessage(String.format("ResourceNotFoundException Empregado id %d not found.", id));
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        } try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            log.sendLogMessage("Database Integrity Violation Exception [deleteById]");
            throw new DatabaseException("Database Integrity Violation");
        }
        log.sendAuditMessage("Empregado ID " + id + "deletado.");
    }

    @Override
    public EmpregadoDTO insert(EmpregadoDTO dto) {
        log.sendLogMessage("Criar empregado [insert]");
        Empregado empregado = empregadoMapper.toEntity(dto);
        Empregado entity = repository.save(empregado);
        log.sendAuditMessage("Empregado ID " + empregado.getId() + "criado.");
        return empregadoMapper.toDto(entity);
    }

    @Override
    public EmpregadoDTO update(Long id, EmpregadoDTO dto) {
        log.sendLogMessage("Atualizar empregado [update]");
        try {
            Empregado entity = repository.getReferenceById(id);
            empregadoMapper.updateEntityFromDto(dto, entity);
            entity = repository.save(entity);
            log.sendAuditMessage("Empregado ID " + entity.getId() + "atualizado.");
            return empregadoMapper.toDto(entity);
        } catch (EntityNotFoundException e) {
            log.sendLogMessage(String.format("Empregado id %d not found.", id));
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        }
    }
}
