package com.squad17.pcdevapi.models.enums;

public enum StatusCandidatura {
    PENDENTE("Pendente"),
    ACEITA("Aceita"),
    RECUSADA("Recusada");

    private String descricao;

    StatusCandidatura(String descricao) {
        this.descricao = descricao;
    }
}
