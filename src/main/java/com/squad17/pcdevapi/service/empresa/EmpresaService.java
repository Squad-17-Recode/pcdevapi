package com.squad17.pcdevapi.service.empresa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaResponseDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import com.squad17.pcdevapi.repository.endereco.EnderecoRepository;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Empresa> findById(UUID id) {
        return empresaRepository.findById(id);
    }

    public Optional<Empresa> findByUsername(String username) {
        return empresaRepository.findByUsername(username);
    }

    public Empresa save(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public boolean existsByUsername(String username) {
        return empresaRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return empresaRepository.existsByEmail(email);
    }

    public Empresa convertToEntity(EmpresaDTO dto) {
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

        Empresa empresa = new Empresa(
            dto.getUsername(),
            dto.getEmail(),
            dto.getSenha(),
            dto.getNome(),
            endereco,
            passwordEncoder,
            dto.getCnpj(),
            dto.getDescricao() != null ? dto.getDescricao() : "",
            dto.getFotoPerfil() != null ? dto.getFotoPerfil() : "",
            contatos,
            dto.getRangeFuncionarios()
        );

        for (Contato contato : contatos) {
            contato.setConta(empresa);
        }

        return empresa;
    }

    public EmpresaResponseDTO convertToResponseDTO(Empresa empresa) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(empresa.getId());
        dto.setUsername(empresa.getUsername());
        dto.setNome(empresa.getNome());
        dto.setEmail(empresa.getEmail());
        return dto;
    }
}
