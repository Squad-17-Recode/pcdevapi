package com.squad17.pcdevapi.repositories;

import com.squad17.pcdevapi.models.candidato.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CandidatoRepository extends JpaRepository<Candidato, UUID> {
    Candidato findByEmail(String email);
    Candidato findByUsername(String username);
}
