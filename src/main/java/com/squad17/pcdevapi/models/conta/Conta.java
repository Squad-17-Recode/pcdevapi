package com.squad17.pcdevapi.models.conta;

public abstract class Conta {
    private String username;
    private String email;
    private String senha;
    private String nome;

    public Conta() {
    }

    public Conta(String username, String email, String senha, String nome) {
        this.username = username;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }
}
