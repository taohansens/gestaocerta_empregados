package com.taohansen.gestaocerta.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArquivoDTO {
    private String id;
    private String nome;
    private String descricao;
    private String filename;
    private String tipoMime;
    private Long tamanho;
    private byte[] conteudo;
}
