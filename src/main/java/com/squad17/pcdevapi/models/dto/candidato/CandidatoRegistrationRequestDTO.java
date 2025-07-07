package com.squad17.pcdevapi.models.dto.candidato;

import lombok.Data;

@Data
public class CandidatoRegisterRequest {
    private String nome;
    private String cpf;
    private String username;
    private String email;
    private String senha;
    private String bio;
    private String enderecoId;
}
