package com.squad17.pcdevapi.service.candidato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
import com.squad17.pcdevapi.repository.endereco.EnderecoRepository;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Candidato> findAll() {
        return candidatoRepository.findAll();
    }

    public Optional<Candidato> findById(UUID id) {
        return candidatoRepository.findById(id);
    }

    public Candidato save(Candidato candidato) {
        return candidatoRepository.save(candidato);
    }

    public void deleteById(UUID id) {
        candidatoRepository.deleteById(id);
    }

    public Candidato convertToEntity(CandidatoDTO dto) {
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
            dto.getFotoPerfil() != null ? dto.getFotoPerfil() : "",
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
            contato.setConta(candidato);
        }

        return candidato;
    }

    public CandidatoResponseDTO convertToResponseDTO(Candidato candidato) {
        CandidatoResponseDTO dto = new CandidatoResponseDTO();
        dto.setId(candidato.getId());
        dto.setUsername(candidato.getUsername());
        dto.setNome(candidato.getNome());
        dto.setEmail(candidato.getEmail());
        return dto;
    }
}
