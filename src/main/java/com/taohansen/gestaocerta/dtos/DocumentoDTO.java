package com.taohansen.gestaocerta.dtos;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class DocumentoDTO {
    private String tipo;
    private String numero;
    private String orgaoEmissor;
    private LocalDate dataEmissao;
}
