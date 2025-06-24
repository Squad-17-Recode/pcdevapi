package com.squad17.pcdevapi.repositories;

import com.squad17.pcdevapi.models.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    Empresa findByEmail(String email);
    Empresa findByUsername(String username);
}
