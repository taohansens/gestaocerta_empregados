package com.taohansen.gestaocerta.controllers;

import com.taohansen.gestaocerta.dtos.EmpregadoDTO;
import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.services.EmpregadoService;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Empregado.class)))}
            )})
    @GetMapping
    public ResponseEntity<List<EmpregadoDTO>> getAll() {
        List<EmpregadoDTO> empregados = service.getAll();
        return ResponseEntity.ok().body(empregados);
    }

    @Operation(summary = "Retorna o empregado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Empregado.class)))})
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmpregadoDTO> findById(@PathVariable Long id) {
        EmpregadoDTO empregadoDTO = service.findById(id);
        return ResponseEntity.ok().body(empregadoDTO);
    }

    @PostMapping
    public ResponseEntity<EmpregadoDTO> insert(@RequestBody EmpregadoDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EmpregadoDTO> update(@PathVariable Long id, @RequestBody EmpregadoDTO dto) {
        EmpregadoDTO entity = service.update(id, dto);
        return ResponseEntity.ok().body(entity);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

