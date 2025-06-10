package com.squad17.pcdevapi.models.vaga;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.squad17.pcdevapi.models.empresa.Empresa;

@Entity
@Table(name = "vaga")
@Data
@NoArgsConstructor
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @NotNull(message = "Empresa é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotNull(message = "Nome do cargo é obrigatório")
    @Size(max = 250, message = "Nome do cargo deve ter no máximo 250 caracteres")
    @Column(name = "nome_cargo", length = 250, nullable = false)
    private String nomeCargo;

    @Size(max = 250, message = "Descrição deve ter no máximo 250 caracteres")
    @Column(name = "descricao", length = 250)
    private String descricao;

    @Column(name = "logo_empresa", columnDefinition = "TEXT")
    private String logoEmpresa;

    @NotNull(message = "Status da vaga é obrigatório")
    @Column(name = "status_vaga", nullable = false)
    private Boolean statusVaga = true;


    @NotNull(message = "Data de fim da candidatura é obrigatória")
    @Column(name = "data_fim_candidatura", nullable = false)
    private LocalDate dataFimCandidatura;

    @NotNull(message = "Data de fim da última etapa é obrigatória")
    @Column(name = "data_fim_ultima_etapa", nullable = false)
    private LocalDate dataFimUltimaEtapa;

    @ElementCollection
    @CollectionTable(name = "vaga_tags", joinColumns = @JoinColumn(name = "vaga_id"))
    @Column(name = "tag")
    private ArrayList<String> tags;

    public Vaga(UUID empresaId,
                String nomeCargo,
                String descricao,
                String logoEmpresa,
                Boolean statusVaga,
                LocalDate dataFimCandidatura,
                LocalDate dataFimUltimaEtapa) {

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

        if (dataFimCandidatura.isAfter(dataFimUltimaEtapa)) {
            throw new IllegalArgumentException("Data fim da candidatura não pode ser posterior à data fim da última etapa");
        }

        this.empresaId = empresaId;
        this.nomeCargo = nomeCargo;
        this.descricao = descricao != null ? descricao : "";
        this.logoEmpresa = logoEmpresa != null ? logoEmpresa : "";
        this.statusVaga = statusVaga != null ? statusVaga : true;
        this.dataFimCandidatura = dataFimCandidatura;
        this.dataFimUltimaEtapa = dataFimUltimaEtapa;
        this.tags = new ArrayList<String>();
    }

    public Vaga(UUID empresaId,
                String nomeCargo,
                LocalDate dataFimCandidatura,
                LocalDate dataFimUltimaEtapa) {
        this(empresaId, nomeCargo, null, null, true, dataFimCandidatura, dataFimUltimaEtapa);
    }

    public boolean isDisponivelParaCandidatura() {
        if (!statusVaga) {
            return false;
        }

        LocalDate hoje = LocalDate.now();
        return !hoje.isAfter(dataFimCandidatura);
    }

    public void encerrarVaga() {
        this.statusVaga = false;
    }

    public void reativarVaga() {
        this.statusVaga = true;
    }

    public boolean isProcessoFinalizado() {
        LocalDate hoje = LocalDate.now();
        return hoje.isAfter(dataFimUltimaEtapa);
    }

    @Override
    public String toString() {
        return String.format("Vaga{id=%s, nomeCargo='%s', empresaId=%s, statusVaga=%s, dataFimCandidatura=%s}",
                id, nomeCargo, empresaId, statusVaga, dataFimCandidatura);
    }
}
