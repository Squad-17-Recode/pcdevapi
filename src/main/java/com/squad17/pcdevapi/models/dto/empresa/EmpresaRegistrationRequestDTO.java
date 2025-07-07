package com.squad17.pcdevapi.models.dto.empresa;

import lombok.Data;

@Data
public class EmpresaRegisterRequest {
    private String cnpj;
    private String descricao;
    private String username;
    private String senha;
    private String email;
    private String fotoPerfil;
    private String bio;
    private String endereco;
}
