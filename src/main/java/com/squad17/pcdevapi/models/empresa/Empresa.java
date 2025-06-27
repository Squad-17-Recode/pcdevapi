package com.squad17.pcdevapi.models.empresa;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;



    public Empresa(String cnpj, String username, String nome, String descricao, String senha, String email, String fotoPerfil, String bio, Endereco endereco, PasswordEncoder passwordEncoder) {
        super(username, email, senha, nome, passwordEncoder);
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.fotoPerfil = fotoPerfil;
        this.setEndereco(endereco);
    }

    public Empresa(String cnpj, String username, String email, String senha, String nome, Endereco endereco, PasswordEncoder passwordEncoder) {
        super(username, email, senha, nome, passwordEncoder);
        this.cnpj = cnpj;
        this.setEndereco(endereco);
    }

    public void setEndereco(String endereco) {
    }
}
