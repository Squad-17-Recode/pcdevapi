package com.squad17.pcdevapi.utils.empresa;

public class EmpresaDataFactory {

    public static String createEmpresaJson(String username, String email, String cnpj, String rangeFuncionarios) {
        return String.format("""
        {
          "username": "%s",
          "email": "%s",
          "senha": "senha123",
          "nome": "Empresa %s",
          "cnpj": "%s",
          "descricao": "Descrição da empresa",
          "rangeFuncionarios": "%s",
          "endereco": {
            "rua": "Rua Teste",
            "bairro": "Centro",
            "cidade": "São Paulo",
            "estado": "SP",
            "cep": "01001000",
            "numero": "100",
            "pais": "Brasil"
          },
          "contatos": [
            { "numeroTelefone": "+55 11 99999-0000" }
          ]
        }
        """, username, email, username, cnpj, rangeFuncionarios);
    }

    public static String createInvalidEmpresaJson(String field, String value) {
        return switch (field) {
            case "email" -> String.format("""
            {
              "username": "testempresa",
              "email": "%s",
              "senha": "senha123",
              "nome": "Test Empresa",
              "cnpj": "12345678000100",
              "rangeFuncionarios": "PEQUENO",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}]
            }
            """, value);
            case "username" -> String.format("""
            {
              %s
              "email": "test@example.com",
              "senha": "senha123",
              "nome": "Test Empresa",
              "cnpj": "12345678000100",
              "rangeFuncionarios": "PEQUENO",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}]
            }
            """, value.isEmpty() ? "" : "\"username\": \"" + value + "\",");
            case "rangeFuncionarios" -> String.format("""
            {
              "username": "testempresa",
              "email": "test@example.com",
              "senha": "senha123",
              "nome": "Test Empresa",
              "cnpj": "12345678000100",
              "rangeFuncionarios": "%s",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}]
            }
            """, value);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }
}
