package com.squad17.pcdevapi.models.dto.empresa;

import java.util.UUID;

import lombok.Data;

@Data
public class EmpresaResponseDTO {
    private UUID id;
    private String username;
    private String nome;
    private String email;
}
