package com.squad17.pcdevapi.models.enums;

//Enum para representar as faixas de funcionários
public enum RangeFuncionarios{
    PEQUENO("1-10"),
    MEDIO("11-100"),
    GRANDE("+100");

    private String descricao;

    RangeFuncionarios(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}