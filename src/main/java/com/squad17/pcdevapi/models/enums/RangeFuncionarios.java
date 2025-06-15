package com.squad17.pcdevapi.models.enums;

import lombok.Getter;

@Getter
public enum RangeFuncionarios {

    PEQUENO("1-10"),

    MEDIO("11-100"),

    GRANDE("+100");

    private final String descricao;

    RangeFuncionarios(String descricao) {
        this.descricao = descricao;
    }
}
