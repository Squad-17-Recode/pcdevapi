package com.squad17.pcdevapi.controllers.candidatura;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<?> getMyCandidaturas(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Número da página deve ser maior ou igual a 1"));
        }

        String username = authentication.getName();
        Candidato candidato = candidatoService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        List<Candidatura> allCandidaturas = candidato.getCandidaturas();
        int totalElements = allCandidaturas.size();
        int fromIndex = Math.min((page - 1) * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<Candidatura> pagedCandidaturas = allCandidaturas.subList(fromIndex, toIndex);

        if (pagedCandidaturas.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Não foi possível encontrar candidaturas"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedCandidaturas);
        response.put("totalElements", totalElements);
        response.put("totalPages", (int) Math.ceil((double) totalElements / size));
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    @PostMapping
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
}
