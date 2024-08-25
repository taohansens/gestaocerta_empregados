package com.taohansen.gestaocerta.dtos;

import com.taohansen.gestaocerta.entities.enums.Estado;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Embeddable
public class EnderecoDTO {
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private Estado estado;
    private String cep;
}
