package com.squad17.pcdevapi.models.vaga;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Entidade JPA que representa uma Vaga de emprego no sistema.
 *
 * Esta classe modela as informações de uma vaga oferecida por uma empresa,
 * incluindo dados como nome do cargo, descrição, período de candidatura
 * e status de disponibilidade.
 *
 * @author Lucas Costa
 * @author Squad 17
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "vaga")
@Data
@NoArgsConstructor
public class Vaga {

    /**
     * Identificador único da vaga.
     * Chave primária gerada automaticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    /**
     * Identificador da empresa responsável pela vaga.
     * Chave estrangeira que referencia a tabela de empresas.
     * Campo obrigatório.
     */
    @NotNull(message = "ID da empresa é obrigatório")
    @Column(name = "empresa_id", columnDefinition = "UUID", nullable = false)
    private UUID empresaId;

    /**
     * Nome do cargo oferecido na vaga.
     * Campo de texto com até 250 caracteres.
     * Campo obrigatório.
     */
    @NotNull(message = "Nome do cargo é obrigatório")
    @Size(max = 250, message = "Nome do cargo deve ter no máximo 250 caracteres")
    @Column(name = "nome_cargo", length = 250, nullable = false)
    private String nomeCargo;

    /**
     * Descrição detalhada da vaga.
     * Campo de texto com até 250 caracteres.
     * Campo opcional que pode conter informações sobre requisitos,
     * responsabilidades e benefícios.
     */
    @Size(max = 250, message = "Descrição deve ter no máximo 250 caracteres")
    @Column(name = "descricao", length = 250)
    private String descricao;

    /**
     * Logo ou identificação visual da empresa.
     * Campo de texto livre para armazenar URL, caminho ou dados da imagem.
     * Campo opcional.
     */
    @Column(name = "logo_empresa", columnDefinition = "TEXT")
    private String logoEmpresa;

    /**
     * Status de disponibilidade da vaga para candidaturas.
     * - true: Vaga está aceitando candidaturas
     * - false: Vaga não está mais aceitando candidaturas
     * Campo obrigatório com valor padrão true.
     */
    @NotNull(message = "Status da vaga é obrigatório")
    @Column(name = "status_vaga", nullable = false)
    private Boolean statusVaga = true;

    /**
     * Data de início do período de candidaturas.
     * Define quando os candidatos podem começar a se inscrever na vaga.
     * Campo obrigatório.
     */
    @NotNull(message = "Data de fim da candidatura é obrigatória")
    @Column(name = "data_fim_candidatura", nullable = false)
    private LocalDate dataFimCandidatura;

    /**
     * Data de encerramento do período de candidaturas.
     * Define até quando os candidatos podem se inscrever na vaga.
     * Campo obrigatório.
     */
    @NotNull(message = "Data de fim da última etapa é obrigatória")
    @Column(name = "data_fim_ultima_etapa", nullable = false)
    private LocalDate dataFimUltimaEtapa;

    @ElementCollection
    @CollectionTable(name = "vaga_tags", joinColumns = @JoinColumn(name = "vaga_id"))
    @Column(name = "tag")
    private ArrayList<String> tags;
    /**
     * Construtor completo para criação de uma nova vaga.
     *
     * @param empresaId ID da empresa responsável pela vaga
     * @param nomeCargo Nome do cargo oferecido
     * @param descricao Descrição detalhada da vaga (opcional)
     * @param logoEmpresa Logo ou identificação da empresa (opcional)
     * @param statusVaga Status de disponibilidade (true = aceitando candidaturas)
     * @param dataFimCandidatura Data limite para candidaturas
     * @param dataFimUltimaEtapa Data de encerramento do processo seletivo
     *
     * @throws IllegalArgumentException se parâmetros obrigatórios forem nulos
     */
    public Vaga(UUID empresaId,
                String nomeCargo,
                String descricao,
                String logoEmpresa,
                Boolean statusVaga,
                LocalDate dataFimCandidatura,
                LocalDate dataFimUltimaEtapa) {

        // Validações de parâmetros obrigatórios
        if (empresaId == null) {
            throw new IllegalArgumentException("ID da empresa não pode ser nulo");
        }
        if (nomeCargo == null || nomeCargo.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo não pode ser nulo ou vazio");
        }
        if (dataFimCandidatura == null) {
            throw new IllegalArgumentException("Data fim da candidatura não pode ser nula");
        }
        if (dataFimUltimaEtapa == null) {
            throw new IllegalArgumentException("Data fim da última etapa não pode ser nula");
        }

        // Validação de lógica de negócio
        if (dataFimCandidatura.isAfter(dataFimUltimaEtapa)) {
            throw new IllegalArgumentException("Data fim da candidatura não pode ser posterior à data fim da última etapa");
        }

        // Atribuição dos valores
        this.empresaId = empresaId;
        this.nomeCargo = nomeCargo.trim();
        this.descricao = descricao != null ? descricao.trim() : null;
        this.logoEmpresa = logoEmpresa;
        this.statusVaga = statusVaga != null ? statusVaga : true;
        this.dataFimCandidatura = dataFimCandidatura;
        this.dataFimUltimaEtapa = dataFimUltimaEtapa;
        this.tags = new ArrayList<String>();
    }

    /**
     * Construtor simplificado para criação de vaga com dados essenciais.
     * Utiliza valores padrão para campos opcionais.
     *
     * @param empresaId ID da empresa responsável pela vaga
     * @param nomeCargo Nome do cargo oferecido
     * @param dataFimCandidatura Data limite para candidaturas
     * @param dataFimUltimaEtapa Data de encerramento do processo seletivo
     */
    public Vaga(UUID empresaId,
                String nomeCargo,
                LocalDate dataFimCandidatura,
                LocalDate dataFimUltimaEtapa) {
        this(empresaId, nomeCargo, null, null, true, dataFimCandidatura, dataFimUltimaEtapa);
    }

    /**
     * Verifica se a vaga está atualmente disponível para candidaturas.
     * Considera tanto o status da vaga quanto a data limite de candidatura.
     *
     * @return true se a vaga está aceitando candidaturas e dentro do prazo
     */
    public boolean isDisponivelParaCandidatura() {
        if (!statusVaga) {
            return false;
        }

        LocalDate hoje = LocalDate.now();
        return !hoje.isAfter(dataFimCandidatura);
    }

    /**
     * Encerra a vaga, impedindo novas candidaturas.
     * Altera o status para false.
     */
    public void encerrarVaga() {
        this.statusVaga = false;
    }

    /**
     * Reativa a vaga para aceitar candidaturas.
     * Altera o status para true.
     * Não verifica se ainda está dentro do prazo de candidatura.
     */
    public void reativarVaga() {
        this.statusVaga = true;
    }

    /**
     * Verifica se o processo seletivo da vaga já foi finalizado.
     *
     * @return true se a data atual é posterior à data fim da última etapa
     */
    public boolean isProcessoFinalizado() {
        LocalDate hoje = LocalDate.now();
        return hoje.isAfter(dataFimUltimaEtapa);
    }

    /**
     * Representação textual da vaga para debugging e logs.
     *
     * @return String contendo informações principais da vaga
     */
    @Override
    public String toString() {
        return String.format("Vaga{id=%s, nomeCargo='%s', empresaId=%s, statusVaga=%s, dataFimCandidatura=%s}",
                id, nomeCargo, empresaId, statusVaga, dataFimCandidatura);
    }
}
