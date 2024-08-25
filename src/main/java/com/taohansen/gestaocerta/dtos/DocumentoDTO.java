package com.taohansen.gestaocerta.dtos;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class DocumentoDTO {
    private String tipo;
    private String numero;
    private String orgaoEmissor;
    private LocalDate dataEmissao;
}
