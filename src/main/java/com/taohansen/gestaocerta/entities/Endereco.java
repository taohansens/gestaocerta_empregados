package com.taohansen.gestaocerta.entities;

import com.taohansen.gestaocerta.entities.enums.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class Endereco {
    @NotBlank
    private String logradouro;
    @Column(name = "end_complemento")
    private String complemento;
    @NotBlank
    private String bairro;
    @NotBlank
    private String cidade;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato #####-###")
    private String cep;
}
