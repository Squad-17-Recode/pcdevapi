package com.squad17.pcdevapi.models.dto.login;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotNull(message = "Username é obrigatório")
    private String username;

    @NotNull(message = "Senha é obrigatória")
    private String senha;
}
