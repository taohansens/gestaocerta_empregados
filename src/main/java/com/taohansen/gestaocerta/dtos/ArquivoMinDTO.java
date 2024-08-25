package com.taohansen.gestaocerta.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArquivoMinDTO {
    private String id;
    private String nome;
    private String tipoMime;
    private Long tamanho;
}
