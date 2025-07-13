package com.squad17.pcdevapi.repository.candidato;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.candidato.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {
    public Page<Candidato> findAll(Pageable pageable);

    Optional<Candidato> findByUsername(String username);

    Optional<Candidato> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
