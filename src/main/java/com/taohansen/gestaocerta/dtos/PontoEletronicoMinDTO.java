package com.taohansen.gestaocerta.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;


@Getter @Setter
public class PontoEletronicoMinDTO {
    private String data;
    private String horaEntrada;
    private String horaSaida;
    private String horasTrabalhadas;
    private String horasExtras;
}
