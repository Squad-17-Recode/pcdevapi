package com.squad17.pcdevapi.service.vaga;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.dto.vaga.VagaDTO;
import com.squad17.pcdevapi.models.dto.vaga.VagaResponseDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.repository.vaga.VagaRepository;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;

    public List<Vaga> findAll() {
        return vagaRepository.findAll();
    }

    public Vaga convertToEntity(VagaDTO dto, Empresa empresa) {
        return new Vaga(
                empresa,
                dto.getNomeCargo(),
                dto.getDescricao(),
                dto.getLogoEmpresa(),
                dto.getStatusVaga(),
                dto.getDataFimCandidatura(),
                dto.getDataFimUltimaEtapa(),
                dto.getTags()
                );
    }

    public Optional<Vaga> findById(UUID id) {
        return vagaRepository.findById(id);
    }

    public Vaga save(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    public VagaResponseDTO convertToResponseDTO(Vaga vaga) {
        VagaResponseDTO.EmpresaSimpleDTO empresaDTO = null;
        if (vaga.getEmpresa() != null) {
            empresaDTO = new VagaResponseDTO.EmpresaSimpleDTO(
                vaga.getEmpresa().getId(),
                vaga.getEmpresa().getNome(),
                vaga.getEmpresa().getFotoPerfil()
            );
        }

        return new VagaResponseDTO(
            vaga.getId(),
            vaga.getNomeCargo(),
            vaga.getDescricao(),
            vaga.getLogoEmpresa(),
            vaga.getStatusVaga(),
            vaga.getDataFimCandidatura(),
            vaga.getDataFimUltimaEtapa(),
            vaga.getTags(),
            empresaDTO
        );
    }
}
