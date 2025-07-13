package com.squad17.pcdevapi.repository.vaga;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.squad17.pcdevapi.models.vaga.Vaga;

public interface VagaRepository extends JpaRepository<Vaga, UUID> {
}
