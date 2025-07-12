package com.squad17.pcdevapi.models.dto.empresa;

import java.util.List;

import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmpresaDTO {
    @NotNull(message = "Username é obrigatório")
    private String username;

    @NotNull(message = "Email é obrigatório")
    private String email;

    @NotNull(message = "Senha é obrigatória")
    private String senha;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "CNPJ da empresa é obrigatório")
    private String cnpj;

    private String descricao;

    private String fotoPerfil;

    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;

    private List<ContatoDTO> contatos;
}
