package com.squad17.pcdevapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.config.JwtUtils;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.dto.change_password.ChangePasswordRequestDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.dto.login.LoginRequestDTO;
import com.squad17.pcdevapi.models.dto.login.LoginResponseDTO;
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
                .orElseGet(() -> candidatoRepository.findByUsername(loginRequest.getUsername()).map(c -> (Conta) c)
                        .orElse(null));

        if (conta == null) {
            conta = empresaRepository.findByEmail(loginRequest.getUsername())
                    .map(e -> (Conta) e)
                    .orElseGet(() -> empresaRepository.findByUsername(loginRequest.getUsername()).map(e -> (Conta) e)
                            .orElse(null));
        }

        if (conta == null || !passwordEncoder.matches(loginRequest.getSenha(), conta.getSenha())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtils.generateToken(conta);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        Conta conta = candidatoRepository.findByUsername(loggedUsername)
                .map(c -> (Conta) c)
                .orElseGet(() -> empresaRepository.findByUsername(loggedUsername).map(e -> (Conta) e).orElse(null));

        conta.setSenha(request.getNovaSenha(), passwordEncoder);
        if (conta instanceof Candidato) {
            candidatoRepository.save((Candidato) conta);
        } else if (conta instanceof Empresa) {
            empresaRepository.save((Empresa) conta);
        }
        return ResponseEntity.ok().build();
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
        return new Endereco(
                null, // O ID será gerado pelo JPA
                dto.getRua(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep(),
                dto.getNumero(),
                dto.getComplemento(),
                dto.getPontoReferencia(),
                dto.getPais());
    }
}
