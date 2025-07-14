package com.squad17.pcdevapi.controllers.candidatura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.dto.candidatura.CandidaturaDTO;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.service.candidato.CandidatoService;
import com.squad17.pcdevapi.service.candidatura.CandidaturaService;
import com.squad17.pcdevapi.service.vaga.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidaturaService candidaturaService;

    @PostMapping
    public ResponseEntity<?> createCandidatura(@Valid @RequestBody CandidaturaDTO candidaturaDTO, Authentication authentication) {
        String username = authentication.getName();

        Candidato candidato = candidatoService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        Vaga vaga = vagaService.findById(candidaturaDTO.getVagaId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        Candidatura candidatura = new Candidatura(candidato, vaga);
        Candidatura savedCandidatura = candidaturaService.save(candidatura);

        candidato.getCandidaturas().add(candidatura);

        return ResponseEntity.ok(savedCandidatura);
    }
}
