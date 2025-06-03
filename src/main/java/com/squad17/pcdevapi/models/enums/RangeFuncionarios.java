package com.squad17.pcdevapi.models.enums;

import lombok.Getter;

/**
 * Enum que representa as faixas de quantidade de funcionários de uma empresa.
 * Utilizado para categorizar empresas por porte baseado no número de colaboradores.
 *
 * @author Squad 17
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum RangeFuncionarios {

    /**
     * Empresa de pequeno porte com 1 a 10 funcionários.
     */
    PEQUENO("1-10"),

    /**
     * Empresa de médio porte com 11 a 100 funcionários.
     */
    MEDIO("11-100"),

    /**
     * Empresa de grande porte com mais de 100 funcionários.
     */
    GRANDE("+100");

    /**
     * Descrição textual da faixa de funcionários.
     */
    private final String descricao;

    /**
     * Construtor do enum que define a descrição da faixa.
     *
     * @param descricao descrição textual da quantidade de funcionários
     */
    RangeFuncionarios(String descricao) {
        this.descricao = descricao;
    }
}
