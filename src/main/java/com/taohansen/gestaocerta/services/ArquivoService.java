package com.taohansen.gestaocerta.services;

import com.taohansen.gestaocerta.dtos.ArquivoDTO;
import com.taohansen.gestaocerta.dtos.ArquivoMinDTO;
import com.taohansen.gestaocerta.entities.Arquivo;
import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.mappers.ArquivoMapper;
import com.taohansen.gestaocerta.repositories.ArquivoRepository;
import com.taohansen.gestaocerta.repositories.EmpregadoRepository;
import com.taohansen.gestaocerta.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArquivoService {
    private final ArquivoRepository repository;
    private final EmpregadoRepository empregadoRepository;
    private final ArquivoMapper arquivoMapper;

    public ArquivoMinDTO insert(Long empregadoId, ArquivoDTO dto, MultipartFile file) throws IOException {
        Empregado empregado = empregadoRepository.findById(empregadoId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado", empregadoId)));
        Arquivo arquivo = arquivoMapper.toEntity(dto);
        arquivo.setTipoMime(file.getContentType());
        arquivo.setConteudo(file.getBytes());
        arquivo.setTamanho(file.getSize());
        arquivo.setEmpregadoId(empregado.getId());
        repository.save(arquivo);
        return arquivoMapper.toMinDto(arquivo);
    }

    public List<ArquivoMinDTO> getByEmpregadoId(Long empregadoId) {
        List<Arquivo> arquivos = repository.findByEmpregadoId(empregadoId);
        return arquivos.stream()
                .map(arquivoMapper::toMinDto)
                .collect(Collectors.toList());
    }

    //TODO adjust to Return DTO
    public Arquivo baixarArquivo(String arquivoId) {
        return repository.findById(arquivoId)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado"));
    }
}
