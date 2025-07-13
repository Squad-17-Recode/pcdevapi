package com.squad17.pcdevapi.controller.empresa;

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

import com.squad17.pcdevapi.models.dto.empresa.EmpresaResponseDTO;
import com.squad17.pcdevapi.models.dto.vaga.VagaDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.service.empresa.EmpresaService;
import com.squad17.pcdevapi.service.vaga.VagaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private VagaService vagaService;

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> getEmpresaById(@PathVariable UUID id) {
        return empresaService.findById(id)
                .map(empresaService::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> createEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        if (empresaService.existsByUsername(empresaDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (empresaService.existsByEmail(empresaDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Empresa empresa = empresaService.convertToEntity(empresaDTO);
        Empresa savedEmpresa = empresaService.save(empresa);
        return ResponseEntity.ok(empresaService.convertToResponseDTO(savedEmpresa));
    }

    @PostMapping("/vaga")
    public ResponseEntity<?> createVaga(@Valid @RequestBody VagaDTO vagaDTO, Authentication authentication) {
        String username = authentication.getName();
        Empresa empresa = empresaService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Vaga vaga = vagaService.convertToEntity(vagaDTO, empresa);

        Vaga savedVaga = vagaService.save(vaga);
        return ResponseEntity.ok(savedVaga);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }
}
