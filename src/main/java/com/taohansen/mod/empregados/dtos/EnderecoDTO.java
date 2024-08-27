package com.taohansen.mod.empregados.dtos;

import com.taohansen.mod.empregados.entities.enums.Estado;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EnderecoDTO {
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private Estado estado;
    private String cep;
}
