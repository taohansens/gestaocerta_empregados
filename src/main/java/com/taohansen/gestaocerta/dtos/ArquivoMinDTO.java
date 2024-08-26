package com.taohansen.gestaocerta.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArquivoMinDTO {
    private String id;
    private String nome;
    private String descricao;
    private String filename;
    private String tipoMime;
    private Long tamanho;
}
