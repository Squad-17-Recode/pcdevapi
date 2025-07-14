package com.squad17.pcdevapi.controller.candidato;

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
public class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateCandidato() throws Exception {
        String json = """
                {
                  "username": "testuser",
                  "email": "testuser@example.com",
                  "senha": "senhaSegura123",
                  "nome": "Test User",
                  "cpf": "12345678901",
                  "bio": "Bio",
                  "fotoPerfil": "https://example.com/profile.jpg",
                  "endereco": {
                    "rua": "Rua Teste",
                    "bairro": "Centro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "100",
                    "complemento": "Apto 101",
                    "pontoReferencia": "Próximo à praça",
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCandidatoById() throws Exception {
        String json = """
                {
                  "username": "testuser2",
                  "email": "testuser2@example.com",
                  "senha": "senhaSegura123",
                  "nome": "Test User2",
                  "cpf": "12345678902",
                  "bio": "Bio",
                  "fotoPerfil": "https://example.com/profile2.jpg",
                  "endereco": {
                    "rua": "Rua Teste",
                    "bairro": "Centro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "101",
                    "complemento": "Apto 102",
                    "pontoReferencia": "Próximo à praça",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-8889" }
                  ],
                  "habilidades": [
                    { "nome": "Spring", "anosExperiencia": 1 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        String response = mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll(".*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/candidatos/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCandidatoByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCandidatoDuplicateUsername() throws Exception {
        String json = """
                {
                  "username": "dupuser",
                  "email": "dupuser@example.com",
                  "senha": "senha123",
                  "nome": "Dup User",
                  "cpf": "12345678905",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same username
        String jsonDup = """
                {
                  "username": "dupuser",
                  "email": "other@example.com",
                  "senha": "senha123",
                  "nome": "Other User",
                  "cpf": "12345678906",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoDuplicateEmail() throws Exception {
        String json = """
                {
                  "username": "useremail",
                  "email": "useremail@example.com",
                  "senha": "senha123",
                  "nome": "User Email",
                  "cpf": "12345678907",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same email
        String jsonDup = """
                {
                  "username": "otheruser",
                  "email": "useremail@example.com",
                  "senha": "senha123",
                  "nome": "Other User",
                  "cpf": "12345678908",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithInvalidData() throws Exception {
        // Test with missing required fields
        String jsonMissingUsername = """
                {
                  "email": "invalid@example.com",
                  "senha": "senha123",
                  "nome": "Invalid User",
                  "cpf": "12345678910",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
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
                  "cpf": "12345678911",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonInvalidEmail))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllCandidatos() throws Exception {
        // Create a candidato first
        String json = """
                {
                  "username": "getalltest",
                  "email": "getalltest@example.com",
                  "senha": "senha123",
                  "nome": "GetAll Test",
                  "cpf": "12345678909",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Test get all with default pagination
        mockMvc.perform(get("/api/candidatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.currentPage").value(1));

        // Test get all with specific page and size
        mockMvc.perform(get("/api/candidatos?page=1&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllCandidatosInvalidPage() throws Exception {
        mockMvc.perform(get("/api/candidatos?page=0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/candidatos?page=-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllCandidatosPagination() throws Exception {
        // Create two candidatos
        String json1 = """
                {
                  "username": "user1",
                  "email": "user1@example.com",
                  "senha": "senha1",
                  "nome": "User 1",
                  "cpf": "12345678903",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        String json2 = """
                {
                  "username": "user2",
                  "email": "user2@example.com",
                  "senha": "senha2",
                  "nome": "User 2",
                  "cpf": "12345678904",
                  "bio": "Bio",
                  "fotoPerfil": "url",
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
                  ],
                  "habilidades": [
                    { "nome": "Spring", "anosExperiencia": 1 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json2))
                .andExpect(status().isOk());

        // Page 1, size 1
        mockMvc.perform(get("/api/candidatos?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        // Page 2, size 1
        mockMvc.perform(get("/api/candidatos?page=2&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        // Page 3, size 1 (should be 404)
        mockMvc.perform(get("/api/candidatos?page=3&size=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCandidatoWithDifferentTipoDeficiencia() throws Exception {
        // Test AUDITIVA
        String jsonAuditiva = """
                {
                  "username": "candidatoauditiva",
                  "email": "auditiva@example.com",
                  "senha": "senha123",
                  "nome": "Candidato Auditiva",
                  "cpf": "12345678911",
                  "bio": "Candidato com deficiência auditiva",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Auditiva",
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
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonAuditiva))
                .andExpect(status().isOk());

        // Test VISUAL
        String jsonVisual = """
                {
                  "username": "candidatovisual",
                  "email": "visual@example.com",
                  "senha": "senha123",
                  "nome": "Candidato Visual",
                  "cpf": "12345678912",
                  "bio": "Candidato com deficiência visual",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Visual",
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
                  ],
                  "habilidades": [
                    { "nome": "Python", "anosExperiencia": 3 }
                  ],
                  "tipoDeficiencia": "VISUAL"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonVisual))
                .andExpect(status().isOk());

        // Test FISICA
        String jsonFisica = """
                {
                  "username": "candidatofisica",
                  "email": "fisica@example.com",
                  "senha": "senha123",
                  "nome": "Candidato Física",
                  "cpf": "12345678913",
                  "bio": "Candidato com deficiência física",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Física",
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
                  ],
                  "habilidades": [
                    { "nome": "JavaScript", "anosExperiencia": 1 }
                  ],
                  "tipoDeficiencia": "FISICA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonFisica))
                .andExpect(status().isOk());

        // Test INTELECTUAL
        String jsonIntelectual = """
                {
                  "username": "candidatointelectual",
                  "email": "intelectual@example.com",
                  "senha": "senha123",
                  "nome": "Candidato Intelectual",
                  "cpf": "12345678914",
                  "bio": "Candidato com deficiência intelectual",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Intelectual",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "40",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-4444" }
                  ],
                  "habilidades": [
                    { "nome": "HTML", "anosExperiencia": 1 }
                  ],
                  "tipoDeficiencia": "INTELECTUAL"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonIntelectual))
                .andExpect(status().isOk());

        // Test MULTIPLA
        String jsonMultipla = """
                {
                  "username": "candidatomultipla",
                  "email": "multipla@example.com",
                  "senha": "senha123",
                  "nome": "Candidato Múltipla",
                  "cpf": "12345678915",
                  "bio": "Candidato com deficiência múltipla",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Múltipla",
                    "bairro": "Bairro",
                    "cidade": "Cidade",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "50",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-5555" }
                  ],
                  "habilidades": [
                    { "nome": "CSS", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "MULTIPLA"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonMultipla))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithEmptyCollections() throws Exception {
        String json = """
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithMultipleContacts() throws Exception {
        String json = """
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithNullOptionalFields() throws Exception {
        String json = """
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithInvalidTipoDeficiencia() throws Exception {
        String json = """
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithInvalidHabilidadeData() throws Exception {
        String json = """
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

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllCandidatosEmpty() throws Exception {
        // Test when no candidatos exist - should return 404
        mockMvc.perform(get("/api/candidatos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCandidatosWithCustomSize() throws Exception {
        // Create multiple candidatos
        for (int i = 1; i <= 5; i++) {
            String json = String.format("""
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

            mockMvc.perform(post("/api/candidatos")
                    .contentType("application/json")
                    .content(json))
                    .andExpect(status().isOk());
        }

        // Test with different page sizes
        mockMvc.perform(get("/api/candidatos?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(5));

        mockMvc.perform(get("/api/candidatos?page=2&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void testCreateCandidatura_Unauthorized() throws Exception {
        String json = """
                {
                  "vagaId": "00000000-0000-0000-0000-000000000000"
                }
                """;
        mockMvc.perform(post("/api/candidatos/candidatura")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }


}
