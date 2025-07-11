package com.squad17.pcdevapi.controller.candidato;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoDTO;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoResponseDTO;
import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.dto.habilidade.HabilidadeDTO;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.habilidade.Habilidade;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.contato.ContatoRepository;
import com.squad17.pcdevapi.repository.endereco.EnderecoRepository;
import com.squad17.pcdevapi.repository.habilidade.HabilidadeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponseDTO> getCandidatoById(@PathVariable UUID id) {
        return candidatoRepository.findById(id)
                .map(this::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CandidatoResponseDTO> createCandidato(@Valid @RequestBody CandidatoDTO candidatoDTO) {
        if (candidatoRepository.existsByUsername(candidatoDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (candidatoRepository.existsByEmail(candidatoDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Candidato candidato = convertToEntity(candidatoDTO);

        Candidato savedCandidato = candidatoRepository.save(candidato);
        return ResponseEntity.ok(convertToResponseDTO(savedCandidato));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }

    private Candidato convertToEntity(CandidatoDTO dto) {
        EnderecoDTO enderecoDTO = dto.getEndereco();

        Endereco endereco = new Endereco(
                enderecoDTO.getRua(),
                enderecoDTO.getBairro(),
                enderecoDTO.getCidade(),
                enderecoDTO.getEstado(),
                enderecoDTO.getCep(),
                enderecoDTO.getNumero(),
                enderecoDTO.getComplemento(),
                enderecoDTO.getPontoReferencia(),
                enderecoDTO.getPais());

        endereco = enderecoRepository.save(endereco);

        List<Contato> contatos = new ArrayList<>();
        if (dto.getContatos() != null) {
            for (ContatoDTO contatoDTO : dto.getContatos()) {
                Contato contato = new Contato(
                        contatoDTO.getNumeroTelefone()
                );
                contatos.add(contato);
            }
        }

        List<Habilidade> habilidades = new ArrayList<>();
        if (dto.getHabilidades() != null) {
            for (HabilidadeDTO habilidadeDTO : dto.getHabilidades()) {
                Habilidade habilidade = new Habilidade(
                        habilidadeDTO.getNome(),
                        habilidadeDTO.getAnosExperiencia()
                );
                habilidades.add(habilidade);
            }
        }

        Candidato candidato = new Candidato(
            dto.getUsername(),
            dto.getEmail(),
            dto.getSenha(),
            dto.getNome(),
            dto.getBio() != null ? dto.getBio() : "",
            dto.getCpf(),
            endereco,
            dto.getTipoDeficiencia(),
            contatos,
            habilidades,
            passwordEncoder
        );

        for (Habilidade habilidade : habilidades) {
            habilidade.setCandidato(candidato);
        }
        for (Contato contato : contatos) {
            contato.setCandidato(candidato);
        }

        return candidato;
    }

    private CandidatoResponseDTO convertToResponseDTO(Candidato candidato) {
        CandidatoResponseDTO dto = new CandidatoResponseDTO();
        dto.setId(candidato.getId());
        dto.setUsername(candidato.getUsername());
        dto.setNome(candidato.getNome());
        dto.setEmail(candidato.getEmail());
        return dto;
    }
}
