package com.squad17.pcdevapi.models.conta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.endereco.Endereco;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Username é obrigatório")
    @Size(max = 25, message = "Username deve ter no máximo 100 caracteres")
    @Column(name = "username", length = 25, nullable = false, unique = true)
    private String username;

    @NotNull(message = "Email é obrigatório")
    @Size(max = 50, message = "Email deve ter no máximo 250 caracteres")
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @NotNull(message = "Senha é obrigatória")
    @Size(max = 20, message = "Senha deve ter no máximo 100 caracteres")
    @Column(name = "senha", length = 20, nullable = false)
    private String senha;

    @NotNull(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 250 caracteres")
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @NotNull(message = "Endereço é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id", insertable = false, updatable = true, nullable = false)
    private Endereco endereco;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    public void setSenha(String senha, PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senhaPlana, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(senhaPlana, this.senha);
    }

    public Conta(String username, String email, String senha, String nome, PasswordEncoder passwordEncoder) {
        this.username = username;
        this.email = email;
        this.setSenha(senha, passwordEncoder);
        this.nome = nome;
    }
}
