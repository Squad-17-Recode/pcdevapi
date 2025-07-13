package com.squad17.pcdevapi.models.dto.vaga;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VagaDTO {
    @NotNull(message = "Nome do cargo é obrigatório")
    @Size(max = 250, message = "Nome do cargo deve ter no máximo 250 caracteres")
    private String nomeCargo;

    @Size(max = 250, message = "Descrição deve ter no máximo 250 caracteres")
    private String descricao;

    private String logoEmpresa;

    @NotNull(message = "Status da vaga é obrigatório")
    private Boolean statusVaga;

    @NotNull(message = "Data de fim da candidatura é obrigatória")
    private LocalDate dataFimCandidatura;

    @NotNull(message = "Data de fim da última etapa é obrigatória")
    private LocalDate dataFimUltimaEtapa;

    private List<String> tags;
}
