package com.taohansen.mod.empregados.entities;

import com.taohansen.mod.empregados.entities.enums.Sexo;
import jakarta.persistence.*;
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
    private Duration jornadaDiaria;
    private Duration jornadaSemanal;
    @Embedded
    private Documento documento;
    @Embedded
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    private LocalDate nascimento;
}
