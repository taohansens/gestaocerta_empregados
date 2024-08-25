package com.taohansen.gestaocerta.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter @Setter
public class Documento {
    @NotBlank
    @Column(name = "doc_tipo")
    private String tipo;
    @NotBlank
    @Column(name = "doc_numero")
    private String numero;
    @NotBlank
    @Column(name = "doc_emissor")
    private String orgaoEmissor;
    @PastOrPresent
    @Column(name = "doc_emissao")
    private LocalDate dataEmissao;
}
