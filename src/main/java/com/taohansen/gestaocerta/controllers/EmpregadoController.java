package com.taohansen.gestaocerta.controllers;

import com.taohansen.gestaocerta.controllers.exceptions.StandardError;
import com.taohansen.gestaocerta.dtos.EmpregadoDTO;
import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.services.EmpregadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/empregados")
public class EmpregadoController {
    private final EmpregadoService service;

    @Operation(summary = "Retorna uma lista de todos empregados cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista com os empregados cadastrados, vazia ou não", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmpregadoDTO.class)))}
            )})
    @GetMapping
    public ResponseEntity<List<EmpregadoDTO>> getAll() {
        List<EmpregadoDTO> empregados = service.getAll();
        return ResponseEntity.ok().body(empregados);
    }

    @Operation(summary = "Retorna o empregado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o empregado com o respectivo ID", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmpregadoDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Empregado não localizado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))})})
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmpregadoDTO> findById(
            @Parameter(description = "ID do empregado a ser buscado", required = true)
            @PathVariable Long id) {
        EmpregadoDTO empregadoDTO = service.findById(id);
        return ResponseEntity.ok().body(empregadoDTO);
    }

    @Operation(summary = "Criação de empregado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmpregadoDTO.class)))})
    })
    @PostMapping
    public ResponseEntity<EmpregadoDTO> insert(@RequestBody EmpregadoDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(summary = "Atualiza um empregado existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empregado atualizado com sucesso", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmpregadoDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Empregado não encontrado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))})
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<EmpregadoDTO> update(
            @Parameter(description = "ID do empregado a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do empregado", required = true)
            @RequestBody EmpregadoDTO dto) {
        EmpregadoDTO entity = service.update(id, dto);
        return ResponseEntity.ok().body(entity);
    }

    @Operation(summary = "Deleta empregado pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empregado deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Operação não realizada, Empregado Não Encontrado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Operação não realizada, inconsistência no Banco de Dados.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))})
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do empregado a ser buscado", required = true)
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

