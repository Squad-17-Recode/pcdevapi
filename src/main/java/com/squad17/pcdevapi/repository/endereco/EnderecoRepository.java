package com.squad17.pcdevapi.repository.endereco;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.endereco.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
}
