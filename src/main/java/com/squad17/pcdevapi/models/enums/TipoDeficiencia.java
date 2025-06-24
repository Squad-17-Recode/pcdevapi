package com.squad17.pcdevapi.models.enums;

public enum TipoDeficiencia {
    AUDITIVA("Auditiva"),
    VISUAL("Visual"),
    FISICA("Física"),
    INTELECTUAL("Intelectual"),
    MULTIPLA("Múltipla"),
    OUTRA("Outra");

    private final String descricao;

    TipoDeficiencia(String descricao) {
        this.descricao = descricao;
    }

}
