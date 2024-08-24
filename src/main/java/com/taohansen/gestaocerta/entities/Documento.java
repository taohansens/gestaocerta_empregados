package com.taohansen.gestaocerta.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class Documento {
    private String tipo;
    private String numero;
    private String orgaoEmissor;
    private LocalDate dataEmissao;
}
