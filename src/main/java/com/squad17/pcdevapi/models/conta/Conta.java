package com.squad17.pcdevapi.models.conta;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Username é obrigatório")
    @Size(max = 100, message = "Username deve ter no máximo 100 caracteres")
    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @NotNull(message = "Email é obrigatório")
    @Size(max = 250, message = "Email deve ter no máximo 250 caracteres")
    @Column(name = "email", length = 250, nullable = false)
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Size(max = 100, message = "Senha deve ter no máximo 100 caracteres")
    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @NotNull(message = "Nome é obrigatório")
    @Size(max = 250, message = "Nome deve ter no máximo 250 caracteres")
    @Column(name = "nome", length = 250, nullable = false)
    private String nome;

    public Conta(String username, String email, String senha, String nome) {
        this.username = username;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }
}
