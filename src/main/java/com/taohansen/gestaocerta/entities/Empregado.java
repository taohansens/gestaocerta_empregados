package com.taohansen.gestaocerta.entities;

import com.taohansen.gestaocerta.entities.enums.Sexo;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Empregado {
    private Long id;
    private String nome;
    private String cpf;
    private Documento documento;
    private Endereco endereco;
    private Enum<Sexo> sexo;
    private LocalDate nascimento;
}
