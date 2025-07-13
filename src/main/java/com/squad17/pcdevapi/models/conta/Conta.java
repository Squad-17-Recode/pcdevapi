package com.squad17.pcdevapi.models.conta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @NotNull(message = "Contatos não podem ser nulos")
    @Size(min = 1, message = "Deve haver pelo menos um contato")
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos;

    @NotNull(message = "Role é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public void setSenha(String senha, PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senhaPlana, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(senhaPlana, this.senha);
    }

    public Conta(String username, String email, String senha, String nome, Endereco endereco, List<Contato> contatos, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.contatos = contatos;
        this.setSenha(senha, passwordEncoder);
    }
}
