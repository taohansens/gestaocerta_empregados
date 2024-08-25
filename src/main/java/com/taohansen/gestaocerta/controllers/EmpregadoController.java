package com.taohansen.gestaocerta.controllers;

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
    public ResponseEntity<List<Empregado>> getAll() {
        List<Empregado> empregados = service.getAll();
        return ResponseEntity.ok().body(empregados);
    }

    @Operation(summary = "Retorna o empregado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Empregado.class)))})
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Empregado> findById(@PathVariable Long id) {
        Empregado empregado = service.findById(id);
        return ResponseEntity.ok().body(empregado);
    }

    @PostMapping
    public ResponseEntity<Empregado> insert(@RequestBody Empregado dto) {
        Empregado entity = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Empregado> update(@PathVariable Long id, @RequestBody Empregado dto) {
        Empregado entity = service.update(id, dto);
        return ResponseEntity.ok().body(entity);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

