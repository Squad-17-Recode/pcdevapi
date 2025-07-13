package com.squad17.pcdevapi.models.dto.candidatura;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidaturaDTO {
    @NotNull(message = "O ID da vaga é obrigatório")
    private UUID vagaId;
}
