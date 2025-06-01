package com.squad17.pcdevapi.models.empresa;

import java.io.File;
import java.util.UUID;

import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.RangeFuncionarios;

public class Empresa {
    private UUID id; // Identificador único da empresa, gerado automaticamente
    private String cnpj; // CNPJ da empresa, deve ser único
    private String descricao; // Descrição da empresa
    private String fotoPerfil; // URL ou caminho da foto de perfil da empresa
    private String bio; // Biografia ou descrição breve da empresa
    private Endereco endereco; // Endereço da empresa, pode ser null se não houver
    private RangeFuncionarios rangeFuncionarios; // Enum representando a faixa de funcionários da empresa
    private File certificadoCotaPCD; // Arquivo do certificado de cota PCD, pode ser null se não houver
    private Vaga[] vagas; // Lista de vagas de emprego associadas à empresa (FALTA IMPLEMENTAR A CLASSE VAGA)

    // Construtor padrão
    public Empresa() {
        this.id = UUID.randomUUID(); // Gera automaticamente um UUID único
    }

    // Construtor com parâmetros essenciais
    public Empresa(String cnpj, String descricao, RangeFuncionarios rangeFuncionarios) {
        this(); // Chama o construtor padrão para gerar o UUID
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.rangeFuncionarios = rangeFuncionarios;
        this.vagas = new Vaga[0]; // Inicializa array vazio
    }


    // GETTERS E SETTERS

    public UUID getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public RangeFuncionarios getRangeFuncionarios() {
        return rangeFuncionarios;
    }

    public void setRangeFuncionarios(RangeFuncionarios rangeFuncionarios) {
        this.rangeFuncionarios = rangeFuncionarios;
    }

    public File getCertificadoCotaPCD() {
        return certificadoCotaPCD;
    }

    public void setCertificadoCotaPCD(File certificadoCotaPCD) {
        this.certificadoCotaPCD = certificadoCotaPCD;
    }

    public Vaga[] getVagas() {
        return vagas;
    }

    public void setVagas(Vaga[] vagas) {
        this.vagas = vagas;
    }


    // MÉTODOS UTILITÁRIOS

    // Método toString para debug
    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", cnpj='" + cnpj + '\'' +
                ", descricao='" + descricao + '\'' +
                ", endereco=" + endereco +
                ", rangeFuncionarios=" + rangeFuncionarios +
                '}';
    }


    // Adicionar uma vaga
    /**
     * Cria um novo array com tamanho +1 (para comportar a nova vaga)
     * Copia todos os dados existentes para o novo array usando System.arraycopy()
     * Adiciona a nova vaga na última posição do novo array
     * Substitui o array antigo pelo novo array
     * @param novaVaga
     */
    public void adicionarVaga(Vaga novaVaga) {
        Vaga[] novasVagas = new Vaga[vagas.length + 1];
        System.arraycopy(vagas, 0, novasVagas, 0, vagas.length);
        novasVagas[vagas.length] = novaVaga;
        this.vagas = novasVagas;
    }

    // Remover uma vaga
    /**
         * Remove uma vaga do array de vagas da empresa.
         * Cria um novo array com tamanho -1, copia todas as vagas exceto a vaga a ser removida,
         * e substitui o array antigo pelo novo.
         * Se a vaga não for encontrada, o array permanece inalterado.
         * @param vagaRemover a vaga que deve ser removida
         */
    public void removerVaga(Vaga vagaRemover) {
        Vaga[] novasVagas = new Vaga[vagas.length - 1];
        int index = 0;
        for (Vaga vaga : vagas) {
            if (!vaga.equals(vagaRemover)) {
                novasVagas[index++] = vaga;
            }
        }
        this.vagas = novasVagas;
    }
}