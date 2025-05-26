package com.squad17.pcdevapi.models.enums;

public enum TipoDeficiencia {
    AUDITIVA("Auditiva"),
    VISUAL("Visual"),
    FISICA("Física"),
    INTELECTUAL("Intelectual"),
    MULTIPLA("Múltipla"),
    OUTRA("Outra");

    private String descricao;

    TipoDeficiencia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
