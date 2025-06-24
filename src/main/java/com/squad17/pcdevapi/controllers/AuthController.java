package com.squad17.pcdevapi.controllers;

import com.squad17.pcdevapi.config.JwtUtils;
import com.squad17.pcdevapi.models.auth.AuthRequest;
import com.squad17.pcdevapi.models.auth.AuthResponse;
import com.squad17.pcdevapi.models.auth.EmpresaRegisterRequest;
import com.squad17.pcdevapi.models.auth.CandidatoRegisterRequest;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.repositories.EmpresaRepository;
import com.squad17.pcdevapi.repositories.CandidatoRepository;
import com.squad17.pcdevapi.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmpresaRepository empresaRepository;
    private final CandidatoRepository candidatoRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService,
                          JwtUtils jwtUtils,
                          PasswordEncoder passwordEncoder,
                          EmpresaRepository empresaRepository,
                          CandidatoRepository candidatoRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.empresaRepository = empresaRepository;
        this.candidatoRepository = candidatoRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getLogin());
        final String jwt = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup/empresa")
    public ResponseEntity<?> registerEmpresa(@RequestBody EmpresaRegisterRequest request) {
        if (empresaRepository.findByEmail(request.getEmail()) != null ||
                empresaRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }
        Empresa empresa = new Empresa();
        empresa.setCnpj(request.getCnpj());
        empresa.setDescricao(request.getDescricao());
        empresa.setUsername(request.getUsername());
        empresa.setSenha(passwordEncoder.encode(request.getSenha()));
        empresa.setEmail(request.getEmail());
        empresa.setFotoPerfil(request.getFotoPerfil());
        empresa.setBio(request.getBio());
        empresa.setEnderecoId(request.getEnderecoId());
        empresaRepository.save(empresa);
        return ResponseEntity.ok("Empresa cadastrada com sucesso");
    }

    @PostMapping("/signup/candidato")
    public ResponseEntity<?> registerCandidato(@RequestBody CandidatoRegisterRequest request) {
        if (candidatoRepository.findByEmail(request.getEmail()) != null ||
                candidatoRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }
        Candidato candidato = new Candidato();
        candidato.setNome(request.getNome());
        candidato.setCpf(request.getCpf());
        candidato.setUsername(request.getUsername());
        candidato.setEmail(request.getEmail());
        candidato.setSenha(passwordEncoder.encode(request.getSenha()));
        candidato.setBio(request.getBio());
        // Aqui você pode buscar e setar o endereço pelo enderecoId, se necessário
        candidatoRepository.save(candidato);
        return ResponseEntity.ok("Candidato cadastrado com sucesso");
    }
}