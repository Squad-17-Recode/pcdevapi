package com.squad17.pcdevapi.models.vaga;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.empresa.Empresa;

@Entity
@Table(name = "vaga")
@Data
@AllArgsConstructor
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

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidatura> candidaturas;

    @NotNull(message = "Data de fim da candidatura é obrigatória")
    @Column(name = "data_fim_candidatura", nullable = false)
    private LocalDate dataFimCandidatura;

    @NotNull(message = "Data de fim da última etapa é obrigatória")
    @Column(name = "data_fim_ultima_etapa", nullable = false)
    private LocalDate dataFimUltimaEtapa;

    @ElementCollection
    @CollectionTable(name = "vaga_tags", joinColumns = @JoinColumn(name = "vaga_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    public Vaga(Empresa empresa,
                String nomeCargo,
                String descricao,
                String logoEmpresa,
                Boolean statusVaga,
                LocalDate dataFimCandidatura,
                LocalDate dataFimUltimaEtapa,
                List<String> tags) {

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

        this.empresa = empresa;
        this.nomeCargo = nomeCargo;
        this.descricao = descricao != null ? descricao : "";
        this.logoEmpresa = logoEmpresa != null ? logoEmpresa : "";
        this.statusVaga = statusVaga != null ? statusVaga : true;
        this.dataFimCandidatura = dataFimCandidatura;
        this.dataFimUltimaEtapa = dataFimUltimaEtapa;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.candidaturas = new ArrayList<>();
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
        return String.format("Vaga{id=%s, nomeCargo='%s', empresa=%s, statusVaga=%s, dataFimCandidatura=%s}",
                id, nomeCargo, empresa, statusVaga, dataFimCandidatura);
    }
}
