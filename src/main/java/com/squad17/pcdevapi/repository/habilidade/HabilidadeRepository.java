package com.squad17.pcdevapi.repository.habilidade;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.habilidade.Habilidade;

public interface HabilidadeRepository extends JpaRepository<Habilidade, UUID> {
}
