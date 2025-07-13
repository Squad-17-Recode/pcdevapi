package com.squad17.pcdevapi.repository.candidatura;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.candidatura.Candidatura;

public interface CandidaturaRepository extends JpaRepository<Candidatura, UUID> {
}
