package com.squad17.pcdevapi.models.candidato;

import java.util.List;
import java.util.UUID;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;
import com.squad17.pcdevapi.models.experiencia.Experiencia;
import com.squad17.pcdevapi.models.pessoa_fisica.PessoaFisica;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Candidato extends PessoaFisica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @NotNull(message = "Nome de Usuário é obrigatório")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotNull(message = "E-mail da empresa é obrigatório")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotNull(message = "Senha não pode ficar em branco")
    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "bio", length = 500)
    private String bio;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private TipoDeficiencia tipoDeficiencia;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Experiencia> experiencias;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contato> contatos;

    public Candidato() {
    }
    
    public Candidato(String username, String email, String senha, String nome, String cpf, Endereco endereco, TipoDeficiencia tipoDeficiencia, List<Candidatura> candidaturas, List<Experiencia> experiencias, List<Contato> contatos) {
        super(username, email, senha, nome, cpf, contatos);
        this.cpf = cpf;
        this.endereco = endereco;
        this.tipoDeficiencia = tipoDeficiencia;
        this.candidaturas = candidaturas;
        this.experiencias = experiencias;
        this.contatos = contatos;
    }
}