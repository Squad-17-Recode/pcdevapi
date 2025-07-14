package com.squad17.pcdevapi.models.empresa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.RangeFuncionarios;
import com.squad17.pcdevapi.models.enums.Role;
import com.squad17.pcdevapi.models.vaga.Vaga;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "conta_id")
public class Empresa extends Conta {
    @NotNull(message = "CNPJ da empresa é obrigatório")
    @Column(name = "cnpj", length = 250, nullable = false)
    private String cnpj;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Range de funcionários é obrigatório")
    @Column(name = "range_funcionarios", nullable = false)
    private RangeFuncionarios rangeFuncionarios;

    @Column(name = "descricao", length = 250)
    @Size(max = 250, message = "Descrição deve ter no máximo 250 caracteres")
    private String descricao;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vaga> vagas;

    public Empresa(String username, String email, String senha, String nome, Endereco endereco, PasswordEncoder passwordEncoder, String cnpj, String descricao, String fotoPerfil, List<Contato> contatos, RangeFuncionarios rangeFuncionarios) {
        super(username, email, senha, nome, endereco, contatos, passwordEncoder);
        this.cnpj = cnpj;
        this.descricao = descricao != null ? descricao : "";
        this.rangeFuncionarios = rangeFuncionarios;
        this.fotoPerfil = fotoPerfil != null ? fotoPerfil : "";
        this.vagas = new ArrayList<>();
        this.setRole(Role.EMPRESA);
    }
}
