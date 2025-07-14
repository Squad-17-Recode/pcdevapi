package com.squad17.pcdevapi.models.dto.candidato;

import java.util.List;

import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.dto.habilidade.HabilidadeDTO;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidatoDTO {
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Size(max = 11)
    private String cpf;

    @Size(max = 250, message = "Bio deve ter no máximo 250 caracteres")
    private String bio;

    private String fotoPerfil;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;

    @NotNull(message = "Tipo de deficiência é obrigatório")
    private TipoDeficiencia tipoDeficiencia;

    @NotNull(message = "Contatos são obrigatórios")
    @Size(min = 1, message = "Deve haver pelo menos um contato")
    @Valid
    private List<ContatoDTO> contatos;

    @NotNull(message = "Habilidades são obrigatórias")
    @Size(min = 1, message = "Deve haver pelo menos uma habilidade")
    @Valid
    private List<HabilidadeDTO> habilidades;
}
