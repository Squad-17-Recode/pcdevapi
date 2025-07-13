package com.squad17.pcdevapi.controller.candidato;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @BeforeEach
    void cleanUp() {
        candidatoRepository.deleteAll();
    }

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
