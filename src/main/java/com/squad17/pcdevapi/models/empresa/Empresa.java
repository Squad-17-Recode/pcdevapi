package com.squad17.pcdevapi.models.empresa;


import java.io.File;
import java.util.UUID;

import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.enums.RangeFuncionarios;
import lombok.Data;


@Data // Lombok Data for Getters e Setters
public class Empresa {
    private UUID id; // Identificador único da empresa, gerado automaticamente
    private String cnpj; // CNPJ da empresa, deve ser único
    private String descricao; // Descrição da empresa
    private String fotoPerfil; // URL ou caminho da foto de perfil da empresa

    private String bio; // Biografia ou descrição breve da empresa
    private Endereco endereco; // Endereço da empresa, pode ser null se não houver
    private RangeFuncionarios rangeFuncionarios; // Enum representando a faixa de funcionários da empresa
    private File certificadoCotaPCD; // Arquivo do certificado de cota PCD, pode ser null se não houver
    private List<Vaga> vagas; // Lista de vagas de emprego associadas à empresa (FALTA IMPLEMENTAR A CLASSE VAGA)

    // Construtor com parâmetros essenciais
    public Empresa(String cnpj, String descricao, RangeFuncionarios rangeFuncionarios) {
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.rangeFuncionarios = rangeFuncionarios;
        this.vagas = new List<Vaga>(); // Inicializa array vazio
    }


    // MÉTODOS UTILITÁRIOS
    
    // Adicionar uma vaga
    /**
     * Cria um novo array com tamanho +1 (para comportar a nova vaga)
     * Copia todos os dados existentes para o novo array usando System.arraycopy()
     * Adiciona a nova vaga na última posição do novo array
     * Substitui o array antigo pelo novo array
     * @param novaVaga
     */
    public void adicionarVaga(Vaga novaVaga) {
        List<Vaga> novasVagas = new Vaga[vagas.length + 1];
        System.arraycopy(vagas, 0, novasVagas, 0, vagas.length);
        novasVagas[vagas.length] = novaVaga;
        this.vagas = novasVagas;
    }

}

