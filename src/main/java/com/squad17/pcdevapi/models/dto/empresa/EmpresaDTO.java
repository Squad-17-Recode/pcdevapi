package com.squad17.pcdevapi.models.dto.empresa;

import java.util.List;

import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.enums.RangeFuncionarios;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmpresaDTO {
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CNPJ da empresa é obrigatório")
    private String cnpj;

    @NotNull(message = "Range Funcionário é obrigatório")
    private RangeFuncionarios rangeFuncionarios;

    private String descricao;

    private String fotoPerfil;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;

    @Valid
    private List<ContatoDTO> contatos;
}
