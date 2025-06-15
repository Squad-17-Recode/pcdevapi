package com.squad17.pcdevapi.models.candidato;

import java.util.ArrayList;
import java.util.UUID;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;
import com.squad17.pcdevapi.models.experiencia.Experiencia;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
public class Candidato extends Conta {
    @NotNull
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @Size(max = 250, message = "Bio deve ter no máximo 250 caracteres")
    @Column(name = "bio", length = 250)
    private String bio;

    @NotNull(message = "Endereço não pode ser nulo")
    @Column(name = "endereco", nullable = false)
    private Endereco endereco;

    @NotNull(message = "Tipo de deficiência não pode ser nulo")
    @Column(name = "tipo_deficiencia", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeficiencia tipoDeficiencia;

    @ElementCollection
    @CollectionTable(name = "candidato_candidaturas", joinColumns = @JoinColumn(name = "candidato_id"))
    @Column(name = "candidatura")
    private ArrayList<Candidatura> candidaturas;

    @ElementCollection
    @CollectionTable(name = "candidato_experiencias", joinColumns = @JoinColumn(name = "candidato_id"))
    @Column(name = "experiencia")
    private ArrayList<Experiencia> experiencias;

    @ElementCollection
    @CollectionTable(name = "candidato_contatos", joinColumns = @JoinColumn(name = "candidato_id"))
    @Column(name = "contato")
    private ArrayList<Contato> contatos;

    public Candidato(String username, String email, String senha, String nome, String cpf, Endereco endereco, TipoDeficiencia tipoDeficiencia, ArrayList<Candidatura> candidaturas, ArrayList<Experiencia> experiencias, ArrayList<Contato> contatos) {
        super(UUID.randomUUID(), username, email, senha, nome);
        this.cpf = cpf;
        this.endereco = endereco;
        this.tipoDeficiencia = tipoDeficiencia;
        this.candidaturas = candidaturas;
        this.experiencias = experiencias;
        this.contatos = contatos;
    }
}
