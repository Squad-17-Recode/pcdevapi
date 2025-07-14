package com.squad17.pcdevapi.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String candidatoUsername = "candidatotest";
    private String empresaUsername = "empresatest";
    private String password = "senha123";

    @BeforeEach
    void setUp() throws Exception {
        // Create a test candidato
        String candidatoJson = """
                {
                  "username": "%s",
                  "email": "candidato@test.com",
                  "senha": "%s",
                  "nome": "Candidato Test",
                  "cpf": "12345678901",
                  "bio": "Bio test",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Test",
                    "bairro": "Bairro Test",
                    "cidade": "Cidade Test",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "100",
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
                """.formatted(candidatoUsername, password);

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(candidatoJson));

        // Create a test empresa
        String empresaJson = """
                {
                  "username": "%s",
                  "email": "empresa@test.com",
                  "senha": "%s",
                  "nome": "Empresa Test",
                  "cnpj": "12345678000199",
                  "descricao": "Empresa test",
                  "fotoPerfil": "url",
                  "rangeFuncionarios": "GRANDE",
                  "endereco": {
                    "rua": "Rua Empresa",
                    "bairro": "Bairro Empresa",
                    "cidade": "Cidade Empresa",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "200",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-2222" }
                  ]
                }
                """.formatted(empresaUsername, password);

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(empresaJson));
    }

    @Test
    void testLoginCandidatoSuccess() throws Exception {
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "%s"
                }
                """.formatted(candidatoUsername, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginEmpresaSuccess() throws Exception {
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "%s"
                }
                """.formatted(empresaUsername, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginWithInvalidUsername() throws Exception {
        String loginJson = """
                {
                  "username": "nonexistentuser",
                  "senha": "%s"
                }
                """.formatted(password);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "wrongpassword"
                }
                """.formatted(candidatoUsername);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithEmptyCredentials() throws Exception {
        String loginJson = """
                {
                  "username": "",
                  "senha": ""
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithMissingFields() throws Exception {
        String loginJson = """
                {
                  "username": "%s"
                }
                """.formatted(candidatoUsername);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "candidatotest")
    void testChangePasswordSuccess() throws Exception {
        String changePasswordJson = """
                {
                  "novaSenha": "newpassword123"
                }
                """;

        mockMvc.perform(put("/api/auth/change-password")
                .contentType("application/json")
                .content(changePasswordJson))
                .andExpect(status().isOk());

        // Test login with new password
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "newpassword123"
                }
                """.formatted(candidatoUsername);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @WithMockUser(username = "empresatest")
    void testChangePasswordForEmpresa() throws Exception {
        String changePasswordJson = """
                {
                  "novaSenha": "empresanewpass456"
                }
                """;

        mockMvc.perform(put("/api/auth/change-password")
                .contentType("application/json")
                .content(changePasswordJson))
                .andExpect(status().isOk());

        // Test login with new password
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "empresanewpass456"
                }
                """.formatted(empresaUsername);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testChangePasswordWithoutAuthentication() throws Exception {
        String changePasswordJson = """
                {
                  "novaSenha": "newpassword123"
                }
                """;

        mockMvc.perform(put("/api/auth/change-password")
                .contentType("application/json")
                .content(changePasswordJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testChangePasswordWithInvalidData() throws Exception {
        String changePasswordJson = """
                {
                  "novaSenha": ""
                }
                """;

        mockMvc.perform(put("/api/auth/change-password")
                .contentType("application/json")
                .content(changePasswordJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "candidatotest")
    void testDeleteCandidatoAccount() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isOk());

        // Verify account is deleted by trying to login
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "%s"
                }
                """.formatted(candidatoUsername, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "empresatest")
    void testDeleteEmpresaAccount() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isOk());

        // Verify account is deleted by trying to login
        String loginJson = """
                {
                  "username": "%s",
                  "senha": "%s"
                }
                """.formatted(empresaUsername, password);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteAccountWithoutAuthentication() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    void testDeleteNonExistentAccount() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLoginWithInvalidJsonFormat() throws Exception {
        String invalidJson = """
                {
                  "username": "%s",
                  "senha": "%s"
                """.formatted(candidatoUsername, password); // Missing closing brace

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangePasswordWithInvalidJsonFormat() throws Exception {
        String invalidJson = """
                {
                  "novaSenha": "newpass123"
                """; // Missing closing brace

        mockMvc.perform(put("/api/auth/change-password")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithSpecialCharactersInPassword() throws Exception {
        // First create a user with special characters in password
        String specialPassword = "P@ssw0rd!@#$%";
        String candidatoWithSpecialPass = """
                {
                  "username": "specialpassuser",
                  "email": "special@test.com",
                  "senha": "%s",
                  "nome": "Special Pass User",
                  "cpf": "12345678999",
                  "bio": "Bio test",
                  "fotoPerfil": "url",
                  "endereco": {
                    "rua": "Rua Test",
                    "bairro": "Bairro Test",
                    "cidade": "Cidade Test",
                    "estado": "SP",
                    "cep": "01001000",
                    "numero": "100",
                    "complemento": "",
                    "pontoReferencia": "",
                    "pais": "Brasil"
                  },
                  "contatos": [
                    { "numeroTelefone": "+55 11 99999-3333" }
                  ],
                  "habilidades": [
                    { "nome": "Java", "anosExperiencia": 2 }
                  ],
                  "tipoDeficiencia": "AUDITIVA"
                }
                """.formatted(specialPassword);

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(candidatoWithSpecialPass));

        // Test login with special characters
        String loginJson = """
                {
                  "username": "specialpassuser",
                  "senha": "%s"
                }
                """.formatted(specialPassword);

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
