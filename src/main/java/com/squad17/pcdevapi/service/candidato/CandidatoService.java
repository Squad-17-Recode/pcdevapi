package com.squad17.pcdevapi.service.candidato;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoDTO;
import com.squad17.pcdevapi.models.dto.candidato.CandidatoResponseDTO;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.models.habilidade.Habilidade;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Candidato> findAll() {
        return candidatoRepository.findAll();
    }

    public Optional<Candidato> findById(UUID id) {
        return candidatoRepository.findById(id);
    }

    @Transactional
    public Candidato save(Candidato candidato) {
        try {
            log.info("Attempting to save candidato with username: {}", candidato.getUsername());
            log.info("Candidato endereco: {}", candidato.getEndereco());
            log.info("Candidato contatos size: {}", candidato.getContatos() != null ? candidato.getContatos().size() : 0);
            log.info("Candidato habilidades size: {}", candidato.getHabilidades() != null ? candidato.getHabilidades().size() : 0);

            Candidato saved = candidatoRepository.save(candidato);
            log.info("Successfully saved candidato with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error saving candidato: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Candidato convertToEntity(CandidatoDTO candidatoDTO) {
        try {
            log.info("Converting DTO to entity for user: {}", candidatoDTO.getUsername());

            // Create endereco using the constructor but ensure it's a new entity
            Endereco endereco = new Endereco(
                    candidatoDTO.getEndereco().getRua(),
                    candidatoDTO.getEndereco().getBairro(),
                    candidatoDTO.getEndereco().getCidade(),
                    candidatoDTO.getEndereco().getEstado(),
                    candidatoDTO.getEndereco().getCep(),
                    candidatoDTO.getEndereco().getNumero(),
                    candidatoDTO.getEndereco().getComplemento(),
                    candidatoDTO.getEndereco().getPontoReferencia(), // Now we handle this field
                    candidatoDTO.getEndereco().getPais());
            // Ensure the endereco has no ID (new entity)
            endereco.setId(null);

            // Create contatos list
            List<Contato> contatos = candidatoDTO.getContatos().stream()
                    .map(contatoDTO -> {
                        Contato contato = new Contato();
                        contato.setNumeroTelefone(contatoDTO.getNumeroTelefone());
                        contato.setId(null); // Ensure new entity
                        return contato;
                    })
                    .collect(Collectors.toList());

            // Create habilidades list
            List<Habilidade> habilidades = candidatoDTO.getHabilidades().stream()
                    .map(habilidadeDTO -> {
                        Habilidade habilidade = new Habilidade();
                        habilidade.setNome(habilidadeDTO.getNome());
                        habilidade.setAnosExperiencia(habilidadeDTO.getAnosExperiencia());
                        habilidade.setId(null); // Ensure new entity
                        return habilidade;
                    })
                    .collect(Collectors.toList());

            log.info("Creating candidato entity...");

            // Create candidato
            Candidato candidato = new Candidato(
                    candidatoDTO.getUsername(),
                    candidatoDTO.getEmail(),
                    candidatoDTO.getSenha(),
                    candidatoDTO.getNome(),
                    candidatoDTO.getBio(),
                    candidatoDTO.getFotoPerfil(),
                    candidatoDTO.getCpf(),
                    endereco,
                    candidatoDTO.getTipoDeficiencia(),
                    contatos,
                    habilidades,
                    passwordEncoder);

            // Ensure candidato has no ID (new entity)
            candidato.setId(null);

            log.info("Setting bidirectional relationships...");

            // Set bidirectional relationships
            contatos.forEach(contato -> contato.setConta(candidato));
            habilidades.forEach(habilidade -> habilidade.setCandidato(candidato));

            log.info("Entity conversion completed successfully");
            return candidato;
        } catch (Exception e) {
            log.error("Error converting DTO to entity: {}", e.getMessage(), e);
            throw new RuntimeException("Erro na conversão de DTO para entidade", e);
        }
    }

    public CandidatoResponseDTO convertToResponseDTO(Candidato candidato) {
        CandidatoResponseDTO dto = new CandidatoResponseDTO();
        dto.setId(candidato.getId());
        dto.setUsername(candidato.getUsername());
        dto.setNome(candidato.getNome());
        dto.setEmail(candidato.getEmail());
        return dto;
    }

    public Optional<Candidato> findByUsername(String username) {
        return candidatoRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return candidatoRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return candidatoRepository.existsByEmail(email);
    }

    public void deleteByUsername(String username) {
        candidatoRepository.findByUsername(username)
        .ifPresent(candidatoRepository::delete);
    }

}
