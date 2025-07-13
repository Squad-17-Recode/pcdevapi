package com.squad17.pcdevapi.controller.empresa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private VagaService vagaService;

    @GetMapping
    public ResponseEntity<?> getAllEmpresas(@RequestParam(defaultValue = "1") int page) {
        if (page < 1) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Número da página deve ser maior ou igual a 0");
            return ResponseEntity.badRequest().body(error);
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Empresa> empresasPage = empresaService.findAll(pageable);
        if (empresasPage.getContent().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Não foi possível encontrar empresas");
            return ResponseEntity.status(404).body(error);
        }
        List<EmpresaResponseDTO> dtos = empresasPage.getContent().stream()
                .map(empresaService::convertToResponseDTO)
                .toList();
        Map<String, Object> response = new HashMap<>();
        response.put("content", dtos);
        response.put("totalPages", empresasPage.getTotalPages());
        response.put("totalElements", empresasPage.getTotalElements());
        response.put("currentPage", empresasPage.getNumber());
        return ResponseEntity.ok(response);
    }

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
