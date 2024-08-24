package com.taohansen.gestaocerta.controllers;

import com.taohansen.gestaocerta.entities.Empregado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/empregado")
public class EmpregadoController {
    @Operation(summary = "Retorna uma lista de todos empregados cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Empregado.class)))}
            )})
    @GetMapping
    public ResponseEntity<ArrayList<Empregado>> getAll() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Operation(summary = "Retorna o empregado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Empregado.class)))})
    })
    public ResponseEntity<Empregado> getById() {
        return ResponseEntity.ok(new Empregado());
    }
}

