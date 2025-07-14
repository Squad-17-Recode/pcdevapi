package com.squad17.pcdevapi.models.dto.habilidade;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabilidadeDTO {
    @NotBlank(message = "Nome da habilidade é obrigatório")
    @Size(max = 250, message = "Nome da habilidade deve ter no máximo 250 caracteres")
    private String nome;

    @NotNull(message = "Anos de experiência é obrigatório")
    @Min(value = 0, message = "Anos de experiência deve ser maior ou igual a 0")
    private Integer anosExperiencia;
}
