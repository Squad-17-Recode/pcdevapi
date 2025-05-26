package com.squad17.pcdevapi.models.candidato;

import java.util.List;
import java.util.UUID;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.TipoDeficiencia;
import com.squad17.pcdevapi.models.experiencia.Experiencia;
import com.squad17.pcdevapi.models.pessoa_fisica.PessoaFisica;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Candidato extends PessoaFisica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    private String cpf;
    private String username;
    private String email;
    private String senha;
    private String bio;
    private Endereco endereco;
    private TipoDeficiencia tipoDeficiencia;
    private List<Candidatura> candidaturas;
    private List<Experiencia> experiencias;
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
