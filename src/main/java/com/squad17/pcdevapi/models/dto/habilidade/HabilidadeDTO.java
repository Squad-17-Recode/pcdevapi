package com.squad17.pcdevapi.models.dto.habilidade;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabilidadeDTO {
    @NotNull(message = "Nome da habilidade é obrigatório")
    @Size(max = 250, message = "Nome da habilidade deve ter no máximo 250 caracteres")
    private String nome;

    @NotNull(message = "Anos de experiência é obrigatório")
    private Integer anosExperiencia;
}
