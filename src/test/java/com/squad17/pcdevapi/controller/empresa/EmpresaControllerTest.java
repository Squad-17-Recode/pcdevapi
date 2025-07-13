package com.squad17.pcdevapi.controller.empresa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpresaControllerTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanUp() {
        empresaRepository.deleteAll();
    }

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

        mockMvc.perform(post("/api/empresa")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEmpresaById() throws Exception {
        // First, create an empresa
        String json = """
        {
          "username": "empresaTest2",
          "email": "empresaTest2@example.com",
          "senha": "senhaSegura456",
          "nome": "Empresa Teste2",
          "cnpj": "12345678000198",
          "descricao": "Empresa de testes2.",
          "fotoPerfil": "https://example.com/empresa2.jpg",
          "rangeFuncionarios": "MEDIO",
          "endereco": {
            "rua": "Avenida Teste",
            "bairro": "Centro",
            "cidade": "Cidade",
            "estado": "SP",
            "cep": "01001000",
            "numero": "101",
            "complemento": "Sala 102",
            "pontoReferencia": "Próximo ao parque",
            "pais": "Brasil"
          },
          "contatos": [
            { "numeroTelefone": "+55 11 99999-8889" }
          ]
        }
        """;

        String response = mockMvc.perform(post("/api/empresa")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/empresa/" + id))
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
        mockMvc.perform(post("/api/empresa/vaga")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }
}
