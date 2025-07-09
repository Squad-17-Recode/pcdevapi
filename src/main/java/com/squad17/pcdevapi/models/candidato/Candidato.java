package com.squad17.pcdevapi.models.candidato;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.Role;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;
import com.squad17.pcdevapi.models.habilidade.Habilidade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidato")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "conta_id", referencedColumnName = "id")
public class Candidato extends Conta {
    @NotNull
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Size(max = 250, message = "Bio deve ter no máximo 250 caracteres")
    @Column(name = "bio", length = 250)
    private String bio;

    @Column(name = "tipo_deficiencia")
    @Enumerated(EnumType.STRING)
    private TipoDeficiencia tipoDeficiencia;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidatura> candidaturas = new ArrayList<>();

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habilidade> habilidades = new ArrayList<>();

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    public Candidato(String username, String email, String senha, String nome, String cpf, Endereco endereco, PasswordEncoder passwordEncoder) {
        super(username, email, senha, nome, endereco, passwordEncoder);
        this.cpf = cpf;
        this.setRole(Role.CANDIDATO);
    }
}
