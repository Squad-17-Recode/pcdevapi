package com.squad17.pcdevapi.models.dto.empresa;

import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpresaRegistrationRequestDTO {
    @NotBlank(message = "CNPJ é obrigatório")
    @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos")
    private String cnpj;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    private String fotoPerfil;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;
}
