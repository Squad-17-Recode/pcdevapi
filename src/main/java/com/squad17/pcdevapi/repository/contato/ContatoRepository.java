package com.squad17.pcdevapi.repository.contato;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.contato.Contato;

public interface ContatoRepository extends JpaRepository<Contato, UUID> {

}
