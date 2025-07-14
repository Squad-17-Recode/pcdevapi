package com.squad17.pcdevapi.models.dto.change_password;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    @NotBlank(message = "Nova senha é obrigatória")
    private String novaSenha;
}
