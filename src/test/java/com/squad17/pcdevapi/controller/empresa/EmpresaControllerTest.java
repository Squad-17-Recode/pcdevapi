package com.squad17.pcdevapi.controller.empresa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.flyway.enabled=false"
})
public class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateEmpresa() throws Exception {
        String json = """
        {
          "username": "empresaTest",
          "email": "empresaTest@example.com",
          "senha": "senhaSegura456",
          "nome": "Empresa Teste",
          "cnpj": "12345678000199",
          "descricao": "Empresa de testes.",
          "fotoPerfil": "https://example.com/empresa.jpg",
          "rangeFuncionarios": "GRANDE",
          "endereco": {
            "rua": "Avenida Teste",
            "bairro": "Centro",
            "cidade": "Cidade",
            "estado": "SP",
            "cep": "01001000",
            "numero": "100",
            "complemento": "Sala 101",
            "pontoReferencia": "Próximo ao parque",
            "pais": "Brasil"
          },
          "contatos": [
            { "numeroTelefone": "+55 11 99999-8888" }
          ]
        }
        """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEmpresaByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/empresas/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmpresaDuplicateUsername() throws Exception {
        String json = """
                {
                  "username": "dupempresa",
                  "email": "dupempresa@example.com",
                  "senha": "senha123",
                  "nome": "Dup Empresa",
                  "cnpj": "12345678000195",
                  "descricao": "Empresa duplicada",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "GRANDE",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "3",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8890" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same username
        String jsonDup = """
                {
                  "username": "dupempresa",
                  "email": "other@example.com",
                  "senha": "senha123",
                  "nome": "Other Empresa",
                  "cnpj": "12345678000196",
                  "descricao": "Outra empresa",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "4",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8891" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEmpresaDuplicateEmail() throws Exception {
        String json = """
                {
                  "username": "empresaemail",
                  "email": "empresaemail@example.com",
                  "senha": "senha123",
                  "nome": "Empresa Email",
                  "cnpj": "12345678000197",
                  "descricao": "Empresa teste email",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "PEQUENO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "5",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8892" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same email
        String jsonDup = """
                {
                  "username": "otherempresa",
                  "email": "empresaemail@example.com",
                  "senha": "senha123",
                  "nome": "Other Empresa",
                  "cnpj": "12345678000198",
                  "descricao": "Outra empresa",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "6",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8893" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEmpresaWithInvalidData() throws Exception {
        // Test with missing required fields
        String jsonMissingUsername = """
                {
                  "email": "invalid@example.com",
                  "senha": "senha123",
                  "nome": "Invalid Empresa",
                  "cnpj": "12345678000190",
                  "descricao": "Empresa inválida",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "GRANDE",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "1",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-0000" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonMissingUsername))
                .andExpect(status().isBadRequest());

        // Test with invalid email format
        String jsonInvalidEmail = """
                {
                  "username": "testinvalidemail",
                  "email": "invalid-email-format",
                  "senha": "senha123",
                  "nome": "Test Invalid Email",
                  "cnpj": "12345678000191",
                  "descricao": "Empresa teste",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "2",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-0001" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonInvalidEmail))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEmpresas() throws Exception {
        // Create an empresa first
        String json = """
                {
                  "username": "getalltest",
                  "email": "getalltest@example.com",
                  "senha": "senha123",
                  "nome": "GetAll Test",
                  "cnpj": "12345678000192",
                  "descricao": "Empresa para teste",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "PEQUENO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "7",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8894" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Test get all with default pagination
        mockMvc.perform(get("/api/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.currentPage").value(1));

        // Test get all with specific page and size
        mockMvc.perform(get("/api/empresas?page=1&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllEmpresasInvalidPage() throws Exception {
        mockMvc.perform(get("/api/empresas?page=0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/empresas?page=-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEmpresasPagination() throws Exception {
        // Create two empresas
        String json1 = """
                {
                  "username": "empresa1",
                  "email": "empresa1@example.com",
                  "senha": "senha1",
                  "nome": "Empresa 1",
                  "cnpj": "12345678000193",
                  "descricao": "Primeira empresa",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "PEQUENO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "1",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8888" }
                  ]
                }
                """;

        String json2 = """
                {
                  "username": "empresa2",
                  "email": "empresa2@example.com",
                  "senha": "senha2",
                  "nome": "Empresa 2",
                  "cnpj": "12345678000194",
                  "descricao": "Segunda empresa",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "2",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8889" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json2))
                .andExpect(status().isOk());

        // Page 1, size 1
        mockMvc.perform(get("/api/empresas?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        // Page 2, size 1
        mockMvc.perform(get("/api/empresas?page=2&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        // Page 3, size 1 (should be 404)
        mockMvc.perform(get("/api/empresas?page=3&size=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmpresaWithDifferentRangeFuncionarios() throws Exception {
        // Test PEQUENO
        String jsonPequeno = """
                {
                  "username": "empresapequeno",
                  "email": "pequeno@example.com",
                  "senha": "senha123",
                  "nome": "Empresa Pequeno",
                  "cnpj": "11111111000111",
                  "descricao": "Empresa de pequeno porte",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "PEQUENO",
                  "endereco": {
                    "rua": "Rua Pequeno",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "10",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-1111" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonPequeno))
                .andExpect(status().isOk());

        // Test MEDIO
        String jsonMedio = """
                {
                  "username": "empresamedio",
                  "email": "medio@example.com",
                  "senha": "senha123",
                  "nome": "Empresa Medio",
                  "cnpj": "22222222000222",
                  "descricao": "Empresa de medio porte",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua Medio",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "20",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-2222" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonMedio))
                .andExpect(status().isOk());

        // Test GRANDE
        String jsonGrande = """
                {
                  "username": "empresagrande",
                  "email": "grande@example.com",
                  "senha": "senha123",
                  "nome": "Empresa Grande",
                  "cnpj": "33333333000333",
                  "descricao": "Empresa de grande porte",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "GRANDE",
                  "endereco": {
                    "rua": "Rua Grande",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "30",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-3333" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonGrande))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEmpresaWithEmptyCollections() throws Exception {
        String json = """
                {
                  "username": "emptycollections",
                  "email": "empty@example.com",
                  "senha": "senha123",
                  "nome": "Empty Collections",
                  "cnpj": "44444444000444",
                  "descricao": "Empresa sem contatos",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "PEQUENO",
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
                  "contatos": []
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEmpresaWithMultipleContacts() throws Exception {
        String json = """
                {
                  "username": "multiplecontacts",
                  "email": "multiple@example.com",
                  "senha": "senha123",
                  "nome": "Multiple Contacts",
                  "cnpj": "55555555000555",
                  "descricao": "Empresa com multiplos contatos",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "MEDIO",
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
                    { "numeroTelefone": "+55 11 88888-7777" },
                    { "numeroTelefone": "+55 11 77777-8888" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEmpresaWithNullOptionalFields() throws Exception {
        String json = """
                {
                  "username": "nulloptional",
                  "email": "nullopt@example.com",
                  "senha": "senha123",
                  "nome": "Null Optional",
                  "cnpj": "66666666000666",
                  "rangeFuncionarios": "PEQUENO",
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
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEmpresaWithInvalidRangeFuncionarios() throws Exception {
        String json = """
                {
                  "username": "invalidrange",
                  "email": "invalidrange@example.com",
                  "senha": "senha123",
                  "nome": "Invalid Range",
                  "cnpj": "77777777000777",
                  "descricao": "Empresa com range inválido",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "INVALID_RANGE",
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
                  ]
                }
                """;

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEmpresasEmpty() throws Exception {
        // Test when no empresas exist - should return 404
        mockMvc.perform(get("/api/empresas"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllEmpresasWithCustomSize() throws Exception {
        // Create multiple empresas
        for (int i = 1; i <= 5; i++) {
            String json = String.format("""
                    {
                      "username": "empresa%d",
                      "email": "empresa%d@example.com",
                      "senha": "senha%d",
                      "nome": "Empresa %d",
                      "cnpj": "8888888800%03d",
                      "descricao": "Empresa %d para teste",
                      "fotoPerfil": "url",
                      "rangeFuncionarios": "PEQUENO",
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
                      ]
                    }
                    """, i, i, i, i, i, i, i, i, i, i);

            mockMvc.perform(post("/api/empresas")
                    .contentType("application/json")
                    .content(json))
                    .andExpect(status().isOk());
        }

        // Test with different page sizes
        mockMvc.perform(get("/api/empresas?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(5));

        mockMvc.perform(get("/api/empresas?page=2&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void testGetEmpresaById() throws Exception {
        String json = """
                {
                  "username": "testempresa2",
                  "email": "testempresa2@example.com",
                  "senha": "senhaSegura123",
                  "nome": "Test Empresa2",
                  "cnpj": "99999999000199",
                  "descricao": "Empresa para teste ID",
                  "fotoPerfil": "https://example.com/empresa2.jpg",
                  "rangeFuncionarios": "MEDIO",
                  "endereco": {
                    "rua": "Rua Teste",
                    "bairro": "Centro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "101",
                    "complemento": "Sala 102",
                    "pontoReferencia": "Próximo à praça",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8889" }
                  ]
                }
                """;

        String response = mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll(".*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/api/empresas/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateVaga_Unauthenticated() throws Exception {
        String json = """
                {
                  "nomeCargo": "Desenvolvedor Backend",
                  "descricao": "Vaga para desenvolvedor Java.",
                  "logoEmpresa": "https://example.com/logo.png",
                  "statusVaga": true,
                  "dataFimCandidatura": "2025-08-01",
                  "dataFimUltimaEtapa": "2025-08-15",
                  "tags": ["Java", "Spring Boot"]
                }
                """;
        mockMvc.perform(post("/api/empresas/vaga")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }
}
