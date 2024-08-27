package com.taohansen.gestaocerta.controllers;

import com.taohansen.gestaocerta.dtos.PontoEletronicoMinDTO;
import com.taohansen.gestaocerta.entities.PontoEletronico;
import com.taohansen.gestaocerta.services.PontoEletronicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ponto/{empregadoId}")
public class PontoEletronicoController {
    private final PontoEletronicoService pontoEletronicoService;

    @PostMapping("/entrada")
    public ResponseEntity<PontoEletronicoMinDTO> registrarEntrada(@PathVariable Long empregadoId) {
        PontoEletronicoMinDTO ponto = pontoEletronicoService.registrarEntrada(empregadoId);
        return ResponseEntity.status(201).body(ponto);
    }

    @PostMapping("/saida")
    public ResponseEntity<PontoEletronicoMinDTO> registrarSaida(@PathVariable Long empregadoId) {
        PontoEletronicoMinDTO ponto = pontoEletronicoService.registrarSaida(empregadoId);
        return ResponseEntity.ok(ponto);
    }

    @GetMapping
    public ResponseEntity<List<PontoEletronico>> listarPontos(@PathVariable Long empregadoId) {
        List<PontoEletronico> pontos = pontoEletronicoService.listarPontosNaDataPorEmpregado(empregadoId);
        return ResponseEntity.ok(pontos);
    }
}
