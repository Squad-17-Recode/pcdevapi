package com.squad17.pcdevapi.models.empresa;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.endereco.Endereco;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public Empresa(String cnpj, String username, String nome, String descricao, String senha, String email, String fotoPerfil, Endereco endereco, PasswordEncoder passwordEncoder) {
        super(username, email, senha, nome, endereco, passwordEncoder);
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.fotoPerfil = fotoPerfil;
    }
}
