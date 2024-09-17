package com.gestaocerta.microempregados.dtos;

import com.gestaocerta.microempregados.entities.enums.Sexo;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpregadoDTO {
    private Long id;
    private String nome;
    private String cpf;
    @Embedded
    private DocumentoDTO documento;
    @Embedded
    private EnderecoDTO endereco;
    private Sexo sexo;
    private LocalDate nascimento;
    private Integer jornadaDiaria;
    private Integer jornadaSemanal;
}
