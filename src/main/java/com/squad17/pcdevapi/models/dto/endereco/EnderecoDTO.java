package com.squad17.pcdevapi.models.dto.endereco;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnderecoDTO {
    @NotNull(message = "Rua é obrigatória")
    private String rua;

    @NotNull(message = "Bairro é obrigatório")
    private String bairro;

    @NotNull(message = "Cidade é obrigatória")
    private String cidade;

    @NotNull(message = "Estado é obrigatório")
    private String estado;

    @NotNull(message = "CEP é obrigatório")
    private String cep;

    @NotNull(message = "Número é obrigatório")
    private String numero;

    private String complemento;
    private String pontoReferencia;

    @NotNull(message = "País é obrigatório")
    private String pais;
}
