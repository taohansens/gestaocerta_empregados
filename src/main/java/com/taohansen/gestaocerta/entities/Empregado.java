package com.taohansen.gestaocerta.entities;

import com.taohansen.gestaocerta.entities.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Empregado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
    @Embedded
    private Documento documento;
    @Embedded
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    private LocalDate nascimento;
}
