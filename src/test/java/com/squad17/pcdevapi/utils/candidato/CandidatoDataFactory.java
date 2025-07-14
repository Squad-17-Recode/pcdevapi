package com.squad17.pcdevapi.utils.candidato;

public class CandidatoDataFactory {

    public static String createCandidatoJson(String username, String email, String cpf, String tipoDeficiencia) {
        return String.format("""
        {
          "username": "%s",
          "email": "%s",
          "senha": "senha123",
          "nome": "Candidato %s",
          "cpf": "%s",
          "bio": "Bio do candidato",
          "tipoDeficiencia": "%s",
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
          ],
          "habilidades": [
            { "nome": "Java", "anosExperiencia": 2 }
          ]
        }
        """, username, email, username, cpf, tipoDeficiencia);
    }

    // Overloaded method with empty collections
    public static String createCandidatoJsonWithEmptyCollections(String username, String email, String cpf, String tipoDeficiencia) {
        return String.format("""
        {
          "username": "%s",
          "email": "%s",
          "senha": "senha123",
          "nome": "Candidato %s",
          "cpf": "%s",
          "bio": "Bio do candidato",
          "tipoDeficiencia": "%s",
          "endereco": {
            "rua": "Rua Teste",
            "bairro": "Centro",
            "cidade": "São Paulo",
            "estado": "SP",
            "cep": "01001000",
            "numero": "100",
            "pais": "Brasil"
          },
          "contatos": [],
          "habilidades": []
        }
        """, username, email, username, cpf, tipoDeficiencia);
    }

    // Method with multiple contacts and skills
    public static String createCandidatoJsonWithMultipleData(String username, String email, String cpf, String tipoDeficiencia) {
        return String.format("""
        {
          "username": "%s",
          "email": "%s",
          "senha": "senha123",
          "nome": "Candidato %s",
          "cpf": "%s",
          "bio": "Bio do candidato",
          "tipoDeficiencia": "%s",
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
            { "numeroTelefone": "+55 11 99999-6666" },
            { "numeroTelefone": "+55 11 88888-7777" }
          ],
          "habilidades": [
            { "nome": "Java", "anosExperiencia": 3 },
            { "nome": "Spring", "anosExperiencia": 2 },
            { "nome": "Angular", "anosExperiencia": 1 }
          ]
        }
        """, username, email, username, cpf, tipoDeficiencia);
    }

    // Method with minimal required fields only
    public static String createCandidatoJsonMinimal(String username, String email, String cpf, String tipoDeficiencia) {
        return String.format("""
        {
          "username": "%s",
          "email": "%s",
          "senha": "senha123",
          "nome": "Candidato %s",
          "cpf": "%s",
          "tipoDeficiencia": "%s",
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
          ],
          "habilidades": [
            { "nome": "Java", "anosExperiencia": 2 }
          ]
        }
        """, username, email, username, cpf, tipoDeficiencia);
    }

    public static String createInvalidCandidatoJson(String field, String value) {
        return switch (field) {
            case "email" -> String.format("""
            {
              "username": "testcandidato",
              "email": "%s",
              "senha": "senha123",
              "nome": "Test Candidato",
              "cpf": "12345678901",
              "tipoDeficiencia": "AUDITIVA",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}],
              "habilidades": [{"nome": "Java", "anosExperiencia": 2}]
            }
            """, value);
            case "username" -> String.format("""
            {
              %s
              "email": "test@example.com",
              "senha": "senha123",
              "nome": "Test Candidato",
              "cpf": "12345678901",
              "tipoDeficiencia": "AUDITIVA",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}],
              "habilidades": [{"nome": "Java", "anosExperiencia": 2}]
            }
            """, value.isEmpty() ? "" : "\"username\": \"" + value + "\",");
            case "tipoDeficiencia" -> String.format("""
            {
              "username": "testcandidato",
              "email": "test@example.com",
              "senha": "senha123",
              "nome": "Test Candidato",
              "cpf": "12345678901",
              "tipoDeficiencia": "%s",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}],
              "habilidades": [{"nome": "Java", "anosExperiencia": 2}]
            }
            """, value);
            case "habilidades" -> String.format("""
            {
              "username": "testcandidato",
              "email": "test@example.com",
              "senha": "senha123",
              "nome": "Test Candidato",
              "cpf": "12345678901",
              "tipoDeficiencia": "AUDITIVA",
              "endereco": {"rua": "Rua", "bairro": "Bairro", "cidade": "Cidade", "estado": "SP", "cep": "01001000", "numero": "1", "pais": "Brasil"},
              "contatos": [{"numeroTelefone": "+55 11 99999-0000"}],
              "habilidades": [%s]
            }
            """, value);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }

    public static String createCandidatoJsonWithInvalidTipoDeficiencia() {
        return """
                {
                  "username": "invalidtipo",
                  "email": "invalidtipo@example.com",
                  "senha": "senha123",
                  "nome": "Invalid Tipo",
                  "cpf": "12345678919",
                  "bio": "Bio",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "90",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-9999" }
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "INVALID_TYPE"
                }
                """;
    }

    public static String createCandidatoJsonWithInvalidHabilidade() {
        return """
                {
                  "username": "invalidhabilidade",
                  "email": "invalidhab@example.com",
                  "senha": "senha123",
                  "nome": "Invalid Habilidade",
                  "cpf": "12345678920",
                  "bio": "Bio",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "100",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-0000" }
                  ],
                  "habilidades": [
                    { "nome": "", "anosExperiencia": -1 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;
    }

    public static String createCandidatoJsonForIteration(int i) {
        return String.format("""
                {
                  "username": "user%d",
                  "email": "user%d@example.com",
                  "senha": "senha%d",
                  "nome": "User %d",
                  "cpf": "1234567890%d",
                  "bio": "Bio %d",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua %d",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "%d",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 9999%d-000%d" }
                  ],
                  "habilidades": [
                    { "nome": "Skill%d", "anosExperiencia": %d }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """, i, i, i, i, i, i, i, i, i, i, i, i);
    }

    // Overloaded method with empty collections - parameterless version
    public static String createCandidatoJsonWithEmptyCollections() {
        return """
        {
          "username": "emptycollections",
          "email": "empty@example.com",
          "senha": "senha123",
          "nome": "Empty Collections",
          "cpf": "12345678916",
          "bio": "Bio",
          "fotoPerfil": "url",
          "endereco": {
            "rua": "Rua",
            "bairro": "Bairro",
            "cidade": "Cidade",
            "estado": "SP",
            "cep": "01001000",
            "numero": "60",
            "complemento": "",
            "pontoReferencia": "",
            "pais": "Brasil"
          },
          "contatos": [],
          "habilidades": [],
          "tipoDeficiencia": "AUDITIVA"
        }
        """;
    }

    // Method with multiple contacts and skills - parameterless version
    public static String createCandidatoJsonWithMultipleData() {
        return """
        {
          "username": "multiplecontacts",
          "email": "multiple@example.com",
          "senha": "senha123",
          "nome": "Multiple Contacts",
          "cpf": "12345678917",
          "bio": "Bio",
          "fotoPerfil": "url",
          "endereco": {
            "rua": "Rua",
            "bairro": "Bairro",
            "cidade": "Cidade",
            "estado": "SP",
            "cep": "01001000",
            "numero": "70",
            "complemento": "",
            "pontoReferencia": "",
            "pais": "Brasil"
          },
          "contatos": [
            { "numeroTelefone": "+55 11 99999-6666" },
            { "numeroTelefone": "+55 11 88888-7777" }
          ],
          "habilidades": [
            { "nome": "Java", "anosExperiencia": 3 },
            { "nome": "Spring", "anosExperiencia": 2 },
            { "nome": "Angular", "anosExperiencia": 1 }
          ],
          "tipoDeficiencia": "VISUAL"
        }
        """;
    }

    // Method with minimal required fields only - parameterless version
    public static String createCandidatoJsonMinimal() {
        return """
        {
          "username": "nulloptional",
          "email": "nullopt@example.com",
          "senha": "senha123",
          "nome": "Null Optional",
          "cpf": "12345678918",
          "endereco": {
            "rua": "Rua",
            "bairro": "Bairro",
            "cidade": "Cidade",
            "estado": "SP",
            "cep": "01001000",
            "numero": "80",
            "pais": "Brasil"
          },
          "contatos": [
            { "numeroTelefone": "+55 11 99999-8888" }
          ],
          "habilidades": [
            { "nome": "Java", "anosExperiencia": 2 }
          ],
          "tipoDeficiencia": "AUDITIVA"
        }
        """;
    }
}
