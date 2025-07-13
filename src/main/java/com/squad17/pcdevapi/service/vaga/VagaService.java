package com.squad17.pcdevapi.service.vaga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.dto.vaga.VagaDTO;
import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.vaga.Vaga;
import com.squad17.pcdevapi.repository.vaga.VagaRepository;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;

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

    public Vaga save(Vaga vaga) {
        return vagaRepository.save(vaga);
    }
}
