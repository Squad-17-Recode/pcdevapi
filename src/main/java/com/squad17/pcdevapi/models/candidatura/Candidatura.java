package com.squad17.pcdevapi.models.candidatura;

import jakarta.persistence.Id;

import java.util.UUID;

import com.squad17.pcdevapi.models.enums.StatusCandidatura;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class Candidatura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID idCandidato;
    private UUID idVaga;
    private StatusCandidatura status;
}
