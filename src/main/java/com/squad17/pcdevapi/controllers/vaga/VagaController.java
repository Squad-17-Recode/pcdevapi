package com.squad17.pcdevapi.controllers.vaga;

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

    @PostMapping
    public ResponseEntity<?> createVaga(@Valid @RequestBody VagaDTO vagaDTO, Authentication authentication) {
        String username = authentication.getName();
        Empresa empresa = empresaService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Vaga vaga = vagaService.convertToEntity(vagaDTO, empresa);
        Vaga savedVaga = vagaService.save(vaga);
        return ResponseEntity.ok(savedVaga);
    }

}
