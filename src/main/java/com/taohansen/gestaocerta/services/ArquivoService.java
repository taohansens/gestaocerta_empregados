package com.taohansen.gestaocerta.services;

import com.taohansen.gestaocerta.dtos.ArquivoMinDTO;
import com.taohansen.gestaocerta.dtos.ArquivoUploadDTO;
import com.taohansen.gestaocerta.entities.Arquivo;
import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.mappers.ArquivoMapper;
import com.taohansen.gestaocerta.repositories.ArquivoRepository;
import com.taohansen.gestaocerta.repositories.EmpregadoRepository;
import com.taohansen.gestaocerta.services.exceptions.FileAccessException;
import com.taohansen.gestaocerta.services.exceptions.FileManagerException;
import com.taohansen.gestaocerta.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArquivoService {
    private final ArquivoRepository repository;
    private final EmpregadoRepository empregadoRepository;
    private final ArquivoMapper arquivoMapper;

    public ArquivoMinDTO insert(Long empregadoId, ArquivoUploadDTO dto, MultipartFile file) {
        Empregado empregado = empregadoRepository.findById(empregadoId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Empregado %d não encontrado", empregadoId)));
        try {
            Arquivo arquivo = new Arquivo();
            arquivo.setNome(dto.getNome());
            arquivo.setDescricao(dto.getDescricao());
            arquivo.setTipoMime(file.getContentType());
            arquivo.setConteudo(file.getBytes());
            arquivo.setFilename(file.getOriginalFilename());
            arquivo.setTamanho(file.getSize());
            arquivo.setEmpregadoId(empregado.getId());
            arquivo = repository.save(arquivo);
            return arquivoMapper.toMinDto(arquivo);
        } catch (IOException e) {
            throw new FileManagerException(String.format("Erro ao enviar o arquivo %s.", file.getOriginalFilename()));
        }
    }

    public List<ArquivoMinDTO> getByEmpregadoId(Long empregadoId) {
        List<Arquivo> arquivos = repository.findByEmpregadoId(empregadoId);
        return arquivos.stream()
                .map(arquivoMapper::toMinDto)
                .collect(Collectors.toList());
    }

    //TODO adjust to Return DTO
    public Arquivo baixarArquivo(String arquivoId, Long empregadoId) {
        Arquivo arquivo =  repository.findById(arquivoId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Arquivo não encontrado %s", arquivoId)));
        if(Objects.equals(arquivo.getEmpregadoId(), empregadoId)){
            return arquivo;
        } else {
            throw new FileAccessException("Arquivo não pertence ao usuário informado.");
        }
    }
}
