package com.taohansen.gestaocerta.entities;

import com.taohansen.gestaocerta.entities.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Entity
public class Empregado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
    @NotBlank
    private Duration jornadaDiaria = Duration.ofHours(8);
    @NotBlank
    private Duration jornadaSemanal = Duration.ofHours(40);
    @Embedded
    private Documento documento;
    @Embedded
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    private LocalDate nascimento;
}
