package com.squad17.pcdevapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.empresa.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    Optional<Empresa> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCnpj(String cnpj);

}
