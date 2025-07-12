package com.squad17.pcdevapi.controller.empresa;

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

import com.squad17.pcdevapi.models.contato.Contato;
import com.squad17.pcdevapi.models.dto.contato.ContatoDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaDTO;
import com.squad17.pcdevapi.models.dto.empresa.EmpresaResponseDTO;
import com.squad17.pcdevapi.models.dto.endereco.EnderecoDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.endereco.Endereco;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import com.squad17.pcdevapi.repository.endereco.EnderecoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> getEmpresaById(@PathVariable UUID id) {
        return empresaRepository.findById(id)
                .map(this::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> createEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        if (empresaRepository.existsByUsername(empresaDTO.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (empresaRepository.existsByEmail(empresaDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Empresa empresa = convertToEntity(empresaDTO);

        Empresa savedEmpresa = empresaRepository.save(empresa);
        return ResponseEntity.ok(convertToResponseDTO(savedEmpresa));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getClass().getName() + ": " + ex.getMessage());
    }

    private Empresa convertToEntity(EmpresaDTO dto) {
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

        // Esse método vou usar para a parte de vagas
        //// List<Habilidade> habilidades = new ArrayList<>();
        // if (dto.getHabilidades() != null) {
        //     for (HabilidadeDTO habilidadeDTO : dto.getHabilidades()) {
        //         Habilidade habilidade = new Habilidade(
        //                 habilidadeDTO.getNome(),
        //                 habilidadeDTO.getAnosExperiencia()
        //         );
        //         habilidades.add(habilidade);
        //     }
        // }

        Empresa empresa = new Empresa(
            dto.getUsername(),
            dto.getEmail(),
            dto.getSenha(),
            dto.getNome(),
            endereco,
            passwordEncoder,
            dto.getCnpj(),
            dto.getDescricao()
        );

        // Vou usar para as vagas
        // for (Habilidade habilidade : habilidades) {
        //     habilidade.setEmpresa(empresa);
        // }

        // Verificar relação de contatos com empresa
        // for (Contato contato : contatos) {
        //     contato.setEmpresa(empresa);
        // }

        return empresa;
    }

    private EmpresaResponseDTO convertToResponseDTO(Empresa empresa) {
        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(empresa.getId());
        dto.setUsername(empresa.getUsername());
        dto.setNome(empresa.getNome());
        dto.setEmail(empresa.getEmail());
        return dto;
    }



}