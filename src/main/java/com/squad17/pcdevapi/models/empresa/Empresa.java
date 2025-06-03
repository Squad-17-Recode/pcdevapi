package com.squad17.pcdevapi.models.empresa;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entidade que representa uma empresa no sistema.
 * Contém informações básicas da empresa como dados de contato,
 * descrição e foto de perfil.
 *
 * @author Lucas Costa
 * @author squad 17
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
public class Empresa {

    /**
     * Identificador único da empresa.
     * Chave primária gerada automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * CNPJ da empresa.
     * Campo obrigatório com máximo de 250 caracteres.
     */
    @NotNull(message = "CNPJ da empresa é obrigatório")
    @Column(name = "cnpj", length = 250, nullable = false)
    private String cnpj;

    /**
     * Descrição detalhada da empresa.
     * Campo opcional com máximo de 250 caracteres.
     */
    @Column(name = "descricao", length = 250)
    private String descricao;

    /**
     * Senha de acesso da empresa.
     * Campo obrigatório com máximo de 100 caracteres.
     */
    @NotNull(message = "Senha não pode ser nula")
    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    /**
     * Email de contato da empresa.
     * Campo obrigatório com máximo de 100 caracteres.
     */
    @NotNull(message = "E-mail da empresa é obrigatório")
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    /**
     * Foto de perfil da empresa.
     * Campo opcional armazenado como TEXT.
     */
    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    /**
     * Biografia ou informações adicionais da empresa.
     * Campo opcional com máximo de 500 caracteres.
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * ID do endereço associado à empresa.
     * Campo obrigatório com máximo de 500 caracteres.
     */
    @NotNull(message = "ID do endereço é obrigatório")
    @Column(name = "endereco_id", length = 500, nullable = false)
    private String enderecoId;

    /**
     * Construtor completo para criação de uma nova empresa.
     *
     * @param cnpj CNPJ da empresa
     * @param descricao Descrição da empresa
     * @param senha Senha de acesso
     * @param email Email de contato
     * @param fotoPerfil Foto de perfil
     * @param bio Biografia da empresa
     * @param enderecoId ID do endereço
     */
    public Empresa(String cnpj, String descricao, String senha, String email,
                   String fotoPerfil, String bio, String enderecoId) {
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.senha = senha;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.bio = bio;
        this.enderecoId = enderecoId;
    }

    /**
     * Construtor para criação de empresa com campos obrigatórios.
     *
     * @param cnpj CNPJ da empresa
     * @param senha Senha de acesso
     * @param email Email de contato
     * @param enderecoId ID do endereço
     */
    public Empresa(String cnpj, String senha, String email, String enderecoId) {
        this.cnpj = cnpj;
        this.senha = senha;
        this.email = email;
        this.enderecoId = enderecoId;
    }
}
