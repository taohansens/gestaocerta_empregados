package com.taohansen.gestaocerta.services;

import com.taohansen.gestaocerta.dtos.PontoEletronicoMinDTO;
import com.taohansen.gestaocerta.entities.Empregado;
import com.taohansen.gestaocerta.entities.PontoEletronico;
import com.taohansen.gestaocerta.mappers.PontoEletronicoMapper;
import com.taohansen.gestaocerta.repositories.EmpregadoRepository;
import com.taohansen.gestaocerta.repositories.PontoEletronicoRepository;
import com.taohansen.gestaocerta.services.exceptions.PontoEletronicoException;
import com.taohansen.gestaocerta.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PontoEletronicoService {
    private final PontoEletronicoRepository pontoEletronicoRepository;
    private final EmpregadoRepository empregadoRepository;
    private final PontoEletronicoMapper pontoEletronicoMapper;

    public PontoEletronicoMinDTO registrarEntrada(Long empregadoId) {
        Empregado empregado = empregadoRepository.findById(empregadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empregado não encontrado"));

        LocalDate hoje = LocalDate.now();
        Optional<PontoEletronico> pontoEmAberto = pontoEletronicoRepository
                .findByEmpregadoIdAndDataAndHoraSaidaIsNull(empregadoId, hoje);

        if (pontoEmAberto.isPresent()) {
            throw new PontoEletronicoException("Já existe uma entrada registrada sem saída.");
        }

        LocalTime agora = LocalTime.now();

        PontoEletronico ponto = new PontoEletronico();
        ponto.setEmpregado(empregado);
        ponto.setData(hoje);
        ponto.setHoraEntrada(agora);

        PontoEletronico entity = pontoEletronicoRepository.save(ponto);
        return pontoEletronicoMapper.toMinDto(entity);
    }

    public PontoEletronicoMinDTO registrarSaida(Long empregadoId) {
        Empregado empregado = empregadoRepository.findById(empregadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empregado não encontrado"));

        LocalDate hoje = LocalDate.now();
        PontoEletronico ponto = pontoEletronicoRepository.findByEmpregadoIdAndDataAndHoraSaidaIsNull(empregado.getId(), hoje)
                .orElseThrow(() -> new PontoEletronicoException("Registro de ponto de entrada não encontrado para hoje"));

        ponto.setHoraSaida(LocalTime.now());

        PontoEletronico entity = pontoEletronicoRepository.save(ponto);
        return pontoEletronicoMapper.toMinDto(entity);
    }

    public List<PontoEletronico> listarPontosNaDataPorEmpregado(Long empregadoId) {
        return pontoEletronicoRepository.findByEmpregadoIdAndData(empregadoId, LocalDate.now());
    }

    public List<PontoEletronico> listarPontos(Long empregadoId) {
        return pontoEletronicoRepository.findByEmpregadoId(empregadoId);
    }
}
