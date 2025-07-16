package com.squad17.pcdevapi.models.dto.vaga;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VagaResponseDTO {
    private UUID id;
    private String nomeCargo;
    private String descricao;
    private String logoEmpresa;
    private Boolean statusVaga;
    private LocalDate dataFimCandidatura;
    private LocalDate dataFimUltimaEtapa;
    private List<String> tags;

    // Simple empresa info to avoid circular references
    private EmpresaSimpleDTO empresa;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmpresaSimpleDTO {
        private UUID id;
        private String nome;
        private String fotoPerfil;
    }
}
