package com.squad17.pcdevapi.controllers.vaga;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.models.dto.vaga.VagaDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.service.empresa.EmpresaService;
import com.squad17.pcdevapi.service.vaga.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private VagaService vagaService;

    @GetMapping
    public ResponseEntity<?> getAllVagas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Número da página deve ser maior ou igual a 1"));
        }
        List<Vaga> allVagas = vagaService.findAll();
        int totalElements = allVagas.size();
        int fromIndex = Math.min((page - 1) * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<Vaga> pagedVagas = allVagas.subList(fromIndex, toIndex);
        if (pagedVagas.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Não foi possível encontrar vagas"));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedVagas);
        response.put("totalElements", totalElements);
        response.put("totalPages", (int) Math.ceil((double) totalElements / size));
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/minhas-vagas")
    public ResponseEntity<?> getAllMyVagas(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Número da página deve ser maior ou igual a 1"));
        }

        String username = authentication.getName();
        Empresa empresa = empresaService.findByUsername(username).orElse(null);

        if (empresa == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas empresas autenticadas podem acessar suas vagas"));
        }

        List<Vaga> allVagas = empresa.getVagas();
        int totalElements = allVagas.size();
        int fromIndex = Math.min((page - 1) * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<Vaga> pagedVagas = allVagas.subList(fromIndex, toIndex);

        if (pagedVagas.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Não foi possível encontrar vagas"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pagedVagas);
        response.put("totalElements", totalElements);
        response.put("totalPages", (int) Math.ceil((double) totalElements / size));
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vagaId}/candidaturas")
    public ResponseEntity<?> getCandidaturasFromVaga(
            @PathVariable UUID vagaId,
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Número da página deve ser maior ou igual a 1"));
        }

        String username = authentication.getName();
        Empresa empresa = empresaService.findByUsername(username).orElse(null);

        if (empresa == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas empresas autenticadas podem acessar candidaturas de suas vagas"));
        }

        Vaga vaga = empresa.getVagas().stream()
                .filter(v -> v.getId().equals(vagaId))
                .findFirst()
                .orElse(null);

        if (vaga == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Vaga não pertence à empresa autenticada"));
        }

        List<Candidatura> allCandidaturas = vaga.getCandidaturas();
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
    public ResponseEntity<?> createVaga(@Valid @RequestBody VagaDTO vagaDTO, Authentication authentication) {
        String username = authentication.getName();
        Empresa empresa = empresaService.findByUsername(username).orElse(null);

        if (empresa == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas empresas autenticadas podem criar vagas"));
        }

        Vaga vaga = vagaService.convertToEntity(vagaDTO, empresa);
        Vaga savedVaga = vagaService.save(vaga);

        return ResponseEntity.ok(savedVaga);
    }


}
