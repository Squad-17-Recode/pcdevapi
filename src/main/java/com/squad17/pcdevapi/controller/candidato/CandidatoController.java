package com.squad17.pcdevapi.controller.candidato;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoDTO;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoResponseDTO;
import com.squad17.pcdevapi.models.dto.candidatura.CandidaturaDTO;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.service.candidato.CandidatoService;
import com.squad17.pcdevapi.service.candidatura.CandidaturaService;
import com.squad17.pcdevapi.service.vaga.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {
    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidaturaService candidaturaService;

    @PostMapping
    public ResponseEntity<CandidatoResponseDTO> createCandidato(@Valid @RequestBody CandidatoDTO candidatoDTO) {
        if (candidatoService.findAll().stream().anyMatch(c -> c.getUsername().equals(candidatoDTO.getUsername()))) {
            return ResponseEntity.badRequest().build();
        }
        if (candidatoService.findAll().stream().anyMatch(c -> c.getEmail().equals(candidatoDTO.getEmail()))) {
            return ResponseEntity.badRequest().build();
        }

        Candidato candidato = candidatoService.convertToEntity(candidatoDTO);
        Candidato savedCandidato = candidatoService.save(candidato);
        return ResponseEntity.ok(candidatoService.convertToResponseDTO(savedCandidato));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponseDTO> getCandidatoById(@PathVariable UUID id) {
        return candidatoService.findById(id)
                .map(candidatoService::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/candidatura")
    public ResponseEntity<?> createCandidatura(@Valid @RequestBody CandidaturaDTO candidaturaDTO,
            Authentication authentication) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }

}
