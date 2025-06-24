package com.squad17.pcdevapi.models.dto.candidato;

import java.util.List;
import java.util.UUID;

import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidatoDTO {
    @NotNull(message = "Username é obrigatório")
    private String username;

    @NotNull(message = "Email é obrigatório")
    private String email;

    @NotNull(message = "Senha é obrigatória")
    private String senha;

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "CPF é obrigatório")
    @Size(max = 11)
    private String cpf;

    private String bio;

    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;

    // @NotNull(message = "Tipo de deficiência é obrigatório")
    private TipoDeficiencia tipoDeficiencia;

    private List<String> contatos;
    private List<String> habilidades;
    private List<UUID> candidaturas;
}
