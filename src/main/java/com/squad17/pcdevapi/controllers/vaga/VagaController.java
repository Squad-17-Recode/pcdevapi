package com.squad17.pcdevapi.controllers.vaga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
