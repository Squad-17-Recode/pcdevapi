package com.squad17.pcdevapi.models.dto.contato;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContatoDTO {
    @NotNull(message = "Número de telefone é obrigatório")
    @Size(max = 20, message = "Número de telefone deve ter no máximo 20 caracteres")
    private String numeroTelefone;
}
