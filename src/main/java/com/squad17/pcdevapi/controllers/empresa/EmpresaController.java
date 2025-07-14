package com.squad17.pcdevapi.controllers.empresa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.models.dto.empresa.EmpresaDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaResponseDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.service.empresa.EmpresaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<?> getAllEmpresas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Número da página deve ser maior ou igual a 1");
            return ResponseEntity.badRequest().body(error);
        }
        List<Empresa> allEmpresas = empresaService.findAll();
        int totalElements = allEmpresas.size();
        int fromIndex = Math.min((page - 1) * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<Empresa> pagedEmpresas = allEmpresas.subList(fromIndex, toIndex);
        if (pagedEmpresas.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Não foi possível encontrar empresas");
            return ResponseEntity.status(404).body(error);
        }
        List<EmpresaResponseDTO> dtos = pagedEmpresas.stream()
                .map(empresaService::convertToResponseDTO)
                .toList();
        Map<String, Object> response = new HashMap<>();
        response.put("content", dtos);
        response.put("totalPages", (int) Math.ceil((double) totalElements / size));
        response.put("totalElements", totalElements);
        response.put("currentPage", page);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }
}
