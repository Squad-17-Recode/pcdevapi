package com.squad17.pcdevapi.models.endereco;

public class Endereco {
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private String complemento;
    private String pontoReferencia;
    private String pais;

    public Endereco() {
    }

    public Endereco(String rua, String bairro, String cidade, String estado, String cep, String numero,
            String complemento, String pontoReferencia, String pais) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
        this.complemento = complemento;
        this.pontoReferencia = pontoReferencia;
        this.pais = pais;
    }
}
