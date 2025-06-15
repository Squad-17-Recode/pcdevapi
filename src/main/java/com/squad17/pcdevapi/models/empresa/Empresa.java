package com.squad17.pcdevapi.models.empresa;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.endereco.Endereco;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Empresa extends Conta {
    @NotNull(message = "CNPJ da empresa é obrigatório")
    @Column(name = "cnpj", length = 250, nullable = false)
    private String cnpj;

    @Column(name = "descricao", length = 250)
    private String descricao;

    @NotNull(message = "Senha não pode ser nula")
    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @NotNull(message = "E-mail da empresa é obrigatório")
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @NotNull(message = "Endereço é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id", insertable = false, updatable = true, nullable = false)
    private Endereco endereco;

    public Empresa(String cnpj, String username, String nome, String descricao, String senha, String email, String fotoPerfil, String bio, Endereco endereco) {
        super(UUID.randomUUID(), username, email, senha, nome);
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.fotoPerfil = fotoPerfil;
        this.endereco = endereco;
    }

    public Empresa(String cnpj, String username, String email, String senha, String nome, Endereco endereco) {
        super(UUID.randomUUID(), username, email, senha, nome);
        this.cnpj = cnpj;
        this.endereco = endereco;
    }
}
