package com.squad17.pcdevapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.candidato.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {

    Optional<Candidato> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
