package com.squad17.pcdevapi.models.pessoa_fisica;

import java.util.List;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.contato.Contato;

public abstract class PessoaFisica extends Conta {
    private String cpf;
    private List<Contato> contatos;

    public PessoaFisica() {
    }

    public PessoaFisica(String username, String email, String senha, String nome, String cpf, List<Contato> contatos) {
        super(username, email, senha, nome);
        this.cpf = cpf;
        this.contatos = contatos;
    }
}
