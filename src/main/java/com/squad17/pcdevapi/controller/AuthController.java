package com.squad17.pcdevapi.controller;

import com.squad17.pcdevapi.config.JwtUtils;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoRegistrationRequestDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaRegistrationRequestDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.dto.login.LoginRequestDTO;
import com.squad17.pcdevapi.models.dto.login.LoginResponseDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(CandidatoRepository candidatoRepository,
    EmpresaRepository empresaRepository,
    PasswordEncoder passwordEncoder,
    JwtUtils jwtUtils) {
        this.candidatoRepository = candidatoRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return findContaByLogin(loginRequest.getUsername())
                .filter(conta -> passwordEncoder.matches(loginRequest.getSenha(), conta.getSenha()))
                .map(conta -> {
                    String token = jwtUtils.generateToken(conta);
                    return ResponseEntity.ok(new LoginResponseDTO(token));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/signup/empresa")
    public ResponseEntity<?> registerEmpresa(@Valid @RequestBody EmpresaRegistrationRequestDTO request) {
        if (emailExists(request.getEmail()) || usernameExists(request.getUsername())) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }

        Empresa empresa = new Empresa();
        empresa.setCnpj(request.getCnpj());
        empresa.setDescricao(request.getDescricao());
        empresa.setUsername(request.getUsername());
        empresa.setSenha(passwordEncoder.encode(request.getSenha()));
        empresa.setEmail(request.getEmail());
        empresa.setFotoPerfil(request.getFotoPerfil());
        empresa.setEndereco(createEnderecoFromDTO(request.getEndereco()));

        empresaRepository.save(empresa);

        String token = jwtUtils.generateToken(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(token));
    }

    @PostMapping("/signup/candidato")
    public ResponseEntity<?> registerCandidato(@Valid @RequestBody CandidatoRegistrationRequestDTO request) {
        if (emailExists(request.getEmail()) || usernameExists(request.getUsername())) {
            return ResponseEntity.badRequest().body("Email ou username já cadastrado");
        }

        Candidato candidato = new Candidato();
        candidato.setNome(request.getNome());
        candidato.setCpf(request.getCpf());
        candidato.setUsername(request.getUsername());
        candidato.setEmail(request.getEmail());
        candidato.setSenha(passwordEncoder.encode(request.getSenha()));
        candidato.setBio(request.getBio());
        candidato.setEndereco(createEnderecoFromDTO(request.getEndereco()));

        candidatoRepository.save(candidato);

        String token = jwtUtils.generateToken(candidato);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(token));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String username, @RequestParam String novaSenha) {
        return findContaByUsername(username)
                .map(conta -> {
                    conta.setSenha(passwordEncoder.encode(novaSenha));
                    saveConta(conta);
                    return ResponseEntity.ok("Senha alterada com sucesso");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado"));
    }

    private boolean emailExists(String email) {
        return candidatoRepository.findByEmail(email).isPresent() || empresaRepository.findByEmail(email).isPresent();
    }

    private boolean usernameExists(String username) {
        return candidatoRepository.findByUsername(username).isPresent() || empresaRepository.findByUsername(username).isPresent();
    }

    private Optional<Conta> findContaByLogin(String login) {
        return candidatoRepository.findByEmail(login)
                .map(Conta.class::cast)
                .or(() -> empresaRepository.findByEmail(login).map(Conta.class::cast))
                .or(() -> candidatoRepository.findByUsername(login).map(Conta.class::cast))
                .or(() -> empresaRepository.findByUsername(login).map(Conta.class::cast));
    }

    private Optional<Conta> findContaByUsername(String username) {
        return candidatoRepository.findByUsername(username)
                .map(Conta.class::cast)
                .or(() -> empresaRepository.findByUsername(username).map(Conta.class::cast));
    }

    private Endereco createEnderecoFromDTO(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setPontoReferencia(dto.getPontoReferencia());
        endereco.setPais(dto.getPais());
        return endereco;
    }

    private void saveConta(Conta conta) {
        if (conta instanceof Candidato) {
            candidatoRepository.save((Candidato) conta);
        } else if (conta instanceof Empresa) {
            empresaRepository.save((Empresa) conta);
        }
    }
}