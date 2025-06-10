package com.squad17.pcdevapi.models.candidatura;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.squad17.pcdevapi.models.enums.StatusCandidatura;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "candidatura")
@Data
@NoArgsConstructor
public class Candidatura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "candidato_id", columnDefinition = "UUID", nullable = false)
    private UUID idCandidato;

    @Column(name = "vaga_id", columnDefinition = "UUID", nullable = false)
    private UUID idVaga;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCandidatura status;
}
