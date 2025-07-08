package com.squad17.pcdevapi.models.conta;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.endereco.Endereco;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Username é obrigatório")
    @Size(max = 100, message = "Username deve ter no máximo 100 caracteres")
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @NotNull(message = "Email é obrigatório")
    @Size(max = 250, message = "Email deve ter no máximo 250 caracteres")
    @Column(name = "email", length = 250, nullable = false, unique = true)
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Size(max = 100, message = "Senha deve ter no máximo 100 caracteres")
    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @NotNull(message = "Nome é obrigatório")
    @Size(max = 250, message = "Nome deve ter no máximo 250 caracteres")
    @Column(name = "nome", length = 250, nullable = false)
    private String nome;

    @NotNull(message = "Endereço é obrigatório")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    public void setSenha(String senha, PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senhaPlana, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(senhaPlana, this.senha);
    }

    public Conta(String username, String email, String senha, String nome, Endereco endereco, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.email = email;
        this.setSenha(senha, passwordEncoder);
        this.nome = nome;
        this.endereco = endereco;
    }
}
