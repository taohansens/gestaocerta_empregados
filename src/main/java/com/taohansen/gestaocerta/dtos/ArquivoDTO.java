package com.taohansen.gestaocerta.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArquivoDTO {
    private String id;
    private String nome;
    private String tipoMime;
    private Long tamanho;
    private byte[] conteudo;
}
