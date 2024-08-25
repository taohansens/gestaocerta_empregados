package com.taohansen.gestaocerta.controllers;

import com.taohansen.gestaocerta.dtos.ArquivoDTO;
import com.taohansen.gestaocerta.dtos.ArquivoMinDTO;
import com.taohansen.gestaocerta.entities.Arquivo;
import com.taohansen.gestaocerta.services.ArquivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/empregados/{empregadoId}/arquivos")
public class ArquivoController {
    private final ArquivoService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoMinDTO> adicionarArquivo(@PathVariable Long empregadoId,
                                                          @RequestParam("file") MultipartFile file,
                                                          @RequestParam("nome") String nome,
                                                          @RequestParam("tipoMime") String tipoMime) throws IOException {
        ArquivoDTO arquivoDTO = new ArquivoDTO();
        arquivoDTO.setNome(nome);
        arquivoDTO.setTipoMime(tipoMime);
        arquivoDTO.setTamanho(file.getSize());
        ArquivoMinDTO entity = service.insert(empregadoId, arquivoDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @GetMapping
    public ResponseEntity<List<ArquivoMinDTO>> listarArquivos(@PathVariable Long empregadoId) {
        List<ArquivoMinDTO> arquivos = service.getByEmpregadoId(empregadoId);
        return ResponseEntity.ok(arquivos);
    }

    @GetMapping("/{arquivoId}/download")
    public ResponseEntity<Resource> downloadArquivo(@PathVariable String arquivoId) {
        Arquivo arquivo = service.baixarArquivo(arquivoId);
        ByteArrayResource resource = new ByteArrayResource(arquivo.getConteudo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoMime()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNome() + "\"")
                .body(resource);
    }
}

