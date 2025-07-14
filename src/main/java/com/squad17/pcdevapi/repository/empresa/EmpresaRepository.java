package com.squad17.pcdevapi.repository.empresa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.squad17.pcdevapi.models.empresa.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

    Optional<Empresa> findById(UUID id);

    Optional<Empresa> findByUsername(String username);

    Optional<Empresa> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
