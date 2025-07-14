package com.squad17.pcdevapi.models.dto.change_password;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequestDTO {
    @NotBlank(message = "Nova senha não pode estar vazia")
    private String novaSenha;

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
