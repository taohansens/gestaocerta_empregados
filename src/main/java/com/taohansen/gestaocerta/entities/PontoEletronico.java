package com.taohansen.gestaocerta.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class PontoEletronico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate data;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private Duration horasTrabalhadas;
    private Duration horasExtras;


    @ManyToOne
    @JoinColumn(name = "empregado_id", nullable = false)
    private Empregado empregado;
}
