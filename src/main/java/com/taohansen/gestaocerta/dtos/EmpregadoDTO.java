package com.taohansen.gestaocerta.dtos;

import com.taohansen.gestaocerta.entities.enums.Sexo;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter @Setter
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
}
