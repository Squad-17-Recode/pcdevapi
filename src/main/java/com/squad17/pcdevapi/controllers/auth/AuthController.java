package com.squad17.pcdevapi.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.config.JwtUtils;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.dto.change_password.ChangePasswordDTO;
import com.squad17.pcdevapi.models.dto.login.LoginRequestDTO;
import com.squad17.pcdevapi.models.dto.login.LoginResponseDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.service.candidato.CandidatoService;
import com.squad17.pcdevapi.service.empresa.EmpresaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CandidatoService candidatoService;
    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        System.out.println("Login attempt for: " + loginRequest.getUsername());

        // Try to find by username first
        Conta conta = candidatoService.findByUsername(loginRequest.getUsername())
                .map(c -> (Conta) c)
                .orElse(null);

        if (conta == null) {
            conta = empresaService.findByUsername(loginRequest.getUsername())
                    .map(e -> (Conta) e)
                    .orElse(null);
        }

        // If not found by username, try by email
        if (conta == null) {
            System.out.println("Not found by username, trying by email...");
            conta = candidatoService.findByEmail(loginRequest.getUsername())
                    .map(c -> (Conta) c)
                    .orElse(null);
        }

        if (conta == null) {
            conta = empresaService.findByEmail(loginRequest.getUsername())
                    .map(e -> (Conta) e)
                    .orElse(null);
        }

        if (conta == null) {
            System.out.println("Account not found for: " + loginRequest.getUsername());
            return ResponseEntity.status(401).build();
        }

        System.out.println("Found account for: " + conta.getUsername());
        System.out.println("Stored password hash: " + conta.getSenha());
        System.out.println("Input password: " + loginRequest.getSenha());

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getSenha(), conta.getSenha());
        System.out.println("Password matches: " + passwordMatches);

        if (!passwordMatches) {
            System.out.println("Password does not match for: " + conta.getUsername());
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtils.generateToken(conta);
        System.out.println("Login successful for: " + conta.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        Conta conta = candidatoService.findByUsername(loggedUsername)
                .map(c -> (Conta) c)
                .orElseGet(() -> empresaService.findByUsername(loggedUsername).map(e -> (Conta) e).orElse(null));

        if (conta == null) {
            return ResponseEntity.notFound().build();
        }

        conta.setSenha(request.getNovaSenha(), passwordEncoder);
        if (conta instanceof Candidato) {
            candidatoService.save((Candidato) conta);
        } else if (conta instanceof Empresa) {
            empresaService.save((Empresa) conta);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        String loggedUsername = authentication.getName();

        var candidatoOpt = candidatoService.findByUsername(loggedUsername);
        if (candidatoOpt.isPresent()) {
            candidatoService.deleteByUsername(loggedUsername);
            return ResponseEntity.ok().build();
        }

        var empresaOpt = empresaService.findByUsername(loggedUsername);
        if (empresaOpt.isPresent()) {
            empresaService.deleteByUsername(loggedUsername);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
