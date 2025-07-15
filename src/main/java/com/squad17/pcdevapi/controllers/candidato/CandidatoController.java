package com.squad17.pcdevapi.controllers.candidato;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<?> getAllCandidatos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 1) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Número da página deve ser maior ou igual a 1");
            return ResponseEntity.badRequest().body(error);
        }

        List<Candidato> allCandidatos = candidatoService.findAll();
        int totalElements = allCandidatos.size();
        int fromIndex = Math.min((page - 1) * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<Candidato> pagedCandidatos = allCandidatos.subList(fromIndex, toIndex);

        if (pagedCandidatos.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Não foi possível encontrar candidatos");
            return ResponseEntity.status(404).body(error);
        }

        List<CandidatoResponseDTO> dtos = pagedCandidatos.stream()
                .map(candidatoService::convertToResponseDTO)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", dtos);
        response.put("totalPages", (int) Math.ceil((double) totalElements / size));
        response.put("totalElements", totalElements);
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<?> createCandidato(@Valid @RequestBody CandidatoDTO candidatoDTO) {
        try {
            // Check for existing username using efficient repository method
            if (candidatoService.existsByUsername(candidatoDTO.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Username já existe");
                error.put("field", "username");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Check for existing email using efficient repository method
            if (candidatoService.existsByEmail(candidatoDTO.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email já existe");
                error.put("field", "email");
                return ResponseEntity.badRequest().body(error);
            }

            Candidato candidato = candidatoService.convertToEntity(candidatoDTO);
            Candidato savedCandidato = candidatoService.save(candidato);
            return ResponseEntity.ok(candidatoService.convertToResponseDTO(savedCandidato));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno do servidor: " + e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponseDTO> getCandidatoById(@PathVariable UUID id) {
        return candidatoService.findById(id)
                .map(candidatoService::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/candidatura")
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseExceptions(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();
        if (message != null && message.contains("TipoDeficiencia")) {
            errors.put("tipoDeficiencia", "Tipo de deficiência inválido");
        } else {
            errors.put("error", "Dados inválidos no formato JSON");
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }
}
