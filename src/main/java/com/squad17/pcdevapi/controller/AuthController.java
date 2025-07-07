package com.squad17.pcdevapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.squad17.pcdevapi.config.JwtUtils;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.dto.login.LoginRequestDTO;
import com.squad17.pcdevapi.models.dto.login.LoginResponseDTO;
import com.squad17.pcdevapi.models.auth.EmpresaRegisterRequest;
import com.squad17.pcdevapi.models.auth.CandidatoRegisterRequest;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Conta conta = candidatoRepository.findByEmail(loginRequest.getUsername())
                .map(c -> (Conta) c)
                .orElseGet(() -> candidatoRepository.findByUsername(loginRequest.getUsername()).map(c -> (Conta) c).orElse(null));

        if (conta == null) {
            conta = empresaRepository.findByEmail(loginRequest.getUsername())
                    .map(e -> (Conta) e)
                    .orElseGet(() -> empresaRepository.findByUsername(loginRequest.getUsername()).map(e -> (Conta) e).orElse(null));
        }

        if (conta == null || !passwordEncoder.matches(loginRequest.getSenha(), conta.getSenha())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtils.generateToken(conta);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/signup/empresa")
    public ResponseEntity<?> registerEmpresa(@RequestBody EmpresaRegisterRequest request) {
        if (empresaRepository.findByEmail(request.getEmail()).isPresent() ||
                empresaRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }
        Empresa empresa = new Empresa();
        empresa.setCnpj(request.getCnpj());
        empresa.setDescricao(request.getDescricao());
        empresa.setUsername(request.getUsername());
        empresa.setSenha(passwordEncoder.encode(request.getSenha()));
        empresa.setEmail(request.getEmail());
        empresa.setFotoPerfil(request.getFotoPerfil());
        empresa.setEndereco(request.getEndereco());
        empresaRepository.save(empresa);
        return ResponseEntity.ok("Empresa cadastrada com sucesso");
    }

    @PostMapping("/signup/candidato")
    public ResponseEntity<?> registerCandidato(@RequestBody CandidatoRegisterRequest request) {
        if (candidatoRepository.findByEmail(request.getEmail()).isPresent() ||
                candidatoRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }
        Candidato candidato = new Candidato();
        candidato.setNome(request.getNome());
        candidato.setCpf(request.getCpf());
        candidato.setUsername(request.getUsername());
        candidato.setEmail(request.getEmail());
        candidato.setSenha(passwordEncoder.encode(request.getSenha()));
        candidato.setBio(request.getBio());
        candidatoRepository.save(candidato);
        return ResponseEntity.ok("Candidato cadastrado com sucesso");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String username, @RequestParam String novaSenha) {
        // Procura o usuário (empresa ou candidato)
        Conta conta = candidatoRepository.findByUsername(username)
                .map(c -> (Conta) c)
                .orElseGet(() -> empresaRepository.findByUsername(username).map(e -> (Conta) e).orElse(null));

        if (conta == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        conta.setSenha(passwordEncoder.encode(novaSenha));
        if (conta instanceof Candidato) {
            candidatoRepository.save((Candidato) conta);
        } else if (conta instanceof Empresa) {
            empresaRepository.save((Empresa) conta);
        }
        return ResponseEntity.ok("Senha alterada com sucesso");
    }
}