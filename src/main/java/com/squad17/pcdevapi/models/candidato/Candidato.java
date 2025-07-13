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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidato")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "conta_id")
public class Candidato extends Conta {
    @NotNull(message = "CPF não pode ser nulo")
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Size(max = 250, message = "Bio deve ter no máximo 250 caracteres")
    @Column(name = "bio", length = 250)
    private String bio;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    @Column(name = "tipo_deficiencia")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tipo de deficiência não pode ser nulo")
    private TipoDeficiencia tipoDeficiencia;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidatura> candidaturas;

    @NotNull(message = "Habilidades não podem ser nulas")
    @Size(min = 1, message = "Deve haver pelo menos uma habilidade")
    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habilidade> habilidades;


    public Candidato(String username, String email, String senha, String nome, String bio, String fotoPerfil, String cpf, Endereco endereco, TipoDeficiencia tipoDeficiencia, List<Contato> contatos, List<Habilidade> habilidades, PasswordEncoder passwordEncoder) {
        super(username, email, senha, nome, endereco, contatos, passwordEncoder);
        this.cpf = cpf;
        this.bio = bio != null ? bio : "";
        this.fotoPerfil = fotoPerfil != null ? fotoPerfil : "";
        this.tipoDeficiencia = tipoDeficiencia;
        this.habilidades = habilidades;
        this.candidaturas = new ArrayList<>();
        this.setRole(Role.CANDIDATO);
    }
}
