package com.taohansen.gestaocerta.repositories;

import com.taohansen.gestaocerta.entities.PontoEletronico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PontoEletronicoRepository extends JpaRepository<PontoEletronico, Long> {
    List<PontoEletronico> findByEmpregadoIdAndData(Long empregadoId, LocalDate data);

    Optional<PontoEletronico> findByEmpregadoIdAndDataAndHoraSaidaIsNull(Long empregadoId, LocalDate hoje);

    List<PontoEletronico> findByEmpregadoId(Long empregadoId);
}