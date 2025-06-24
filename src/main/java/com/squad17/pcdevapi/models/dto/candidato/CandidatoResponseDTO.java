package com.squad17.pcdevapi.models.dto.candidato;

import java.util.UUID;

import lombok.Data;

@Data
public class CandidatoResponseDTO {
    private UUID id;
    private String username;
    private String nome;
    private String email;
}
