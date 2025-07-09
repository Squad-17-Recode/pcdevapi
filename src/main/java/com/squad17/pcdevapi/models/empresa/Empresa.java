package com.squad17.pcdevapi.models.empresa;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.Role;

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

    public Empresa(String username, String email, String senha, String nome, Endereco endereco, PasswordEncoder passwordEncoder, String cnpj, String descricao) {
        super(username, email, senha, nome, endereco, passwordEncoder);
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.setRole(Role.EMPRESA);
    }
}
