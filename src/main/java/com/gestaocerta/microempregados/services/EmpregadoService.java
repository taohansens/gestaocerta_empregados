package com.gestaocerta.microempregados.services;

import com.gestaocerta.microempregados.dtos.EmpregadoDTO;
import com.gestaocerta.microempregados.entities.Empregado;
import com.gestaocerta.microempregados.mappers.EmpregadoMapper;
import com.gestaocerta.microempregados.repositories.EmpregadoRepository;
import com.gestaocerta.microempregados.services.impl.EmpregadoServiceImpl;
import com.gestaocerta.microempregados.services.exceptions.DatabaseException;
import com.gestaocerta.microempregados.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpregadoService implements EmpregadoServiceImpl {
    private final EmpregadoRepository repository;
    private final LogProducer log;
    private final EmpregadoMapper empregadoMapper;


    @Transactional(readOnly = true)
    public List<EmpregadoDTO> getAll() {
        sendLogMessage("Consulta lista de empregados [GETALL]");
        List<Empregado> empregados = repository.findAll();
        return empregados.stream()
                .map(empregadoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpregadoDTO findById(Long id) {
        sendLogMessage("Consulta empregado [findById]");
        Optional<Empregado> obj = repository.findById(id);
        Empregado entity = obj.orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado.", id)));
        sendAuditMessage(String.format("%s - CONSULTA Empregado ID %d.", LocalDateTime.now(), id));
        return empregadoMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void deleteById(Long id) {
        sendLogMessage("Deleta empregado [deleteById]");
        sendAuditMessage(String.format("%s - Deleta empregado ID %d.", LocalDateTime.now(), id));
        if (!repository.existsById(id)) {
            sendLogMessage(String.format("ResourceNotFoundException Empregado id %d não encontrado.", id));
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        } try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            sendLogMessage("Database Integrity Violation Exception [deleteById]");
            throw new DatabaseException("Database Integrity Violation");
        }
        sendAuditMessage(String.format("%s - DELETADO Empregado ID %d.", LocalDateTime.now(), id));
    }

    @Override
    public EmpregadoDTO insert(EmpregadoDTO dto) {
        sendLogMessage("Criar empregado [insert]");
        Empregado empregado = empregadoMapper.toEntity(dto);
        Empregado entity = repository.save(empregado);
        sendAuditMessage(String.format("%s - CRIADO Empregado ID %d.", LocalDateTime.now(), entity.getId()));
        return empregadoMapper.toDto(entity);
    }

    @Override
    public EmpregadoDTO update(Long id, EmpregadoDTO dto) {
        sendLogMessage("Atualizar empregado [update]");
        try {
            Empregado entity = repository.getReferenceById(id);
            empregadoMapper.updateEntityFromDto(dto, entity);
            entity = repository.save(entity);
            sendAuditMessage(String.format("%s - ATUALIZADO Empregado ID %d.", LocalDateTime.now(), id));
            return empregadoMapper.toDto(entity);
        } catch (EntityNotFoundException e) {
            sendLogMessage(String.format("Empregado id %d not found.", id));
            throw new ResourceNotFoundException(String.format("Empregado id %d not found.", id));
        }
    }

    private void sendLogMessage(String message) {
        try {
            log.sendLogMessage(message);
        } catch (Exception e) {
            System.err.println("Falha ao enviar log para o RabbitMQ: " + e.getMessage());
        }
    }

    private void sendAuditMessage(String message) {
        try {
            log.sendAuditMessage(message);
        } catch (Exception e) {
            System.err.println("Falha ao enviar log para o RabbitMQ: " + e.getMessage());
        }
    }
}
