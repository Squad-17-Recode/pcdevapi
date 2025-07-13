package com.squad17.pcdevapi.models.candidatura;

import java.time.LocalDateTime;
import java.util.UUID;

import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.enums.StatusCandidatura;
import com.squad17.pcdevapi.models.vaga.Vaga;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidatura")
@Data
@NoArgsConstructor
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @NotNull(message = "Candidato é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @NotNull(message = "Vaga é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @NotNull(message = "Status da candidatura é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_candidatura", nullable = false)
    private StatusCandidatura statusCandidatura = StatusCandidatura.PENDENTE;

    @NotNull(message = "Data de criação é obrigatória")
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Candidatura(Candidato candidato, Vaga vaga) {
        this.candidato = candidato;
        this.vaga = vaga;
        this.statusCandidatura = StatusCandidatura.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }
}
