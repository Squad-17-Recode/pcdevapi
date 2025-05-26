package com.squad17.pcdevapi.models.enums;

public enum StatusCandidatura {
    PENDENTE("Pendente"),
    ACEITA("Aceita"),
    RECUSADA("Recusada");

    private String descricao;

    StatusCandidatura(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
