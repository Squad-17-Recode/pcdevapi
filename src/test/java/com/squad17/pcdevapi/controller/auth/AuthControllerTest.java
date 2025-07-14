package com.squad17.pcdevapi.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squad17.pcdevapi.utils.TestDataFactory;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=false",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.flyway.enabled=false"
})
@Transactional
@Rollback
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Login Tests

    @Test
    void testLoginCandidatoSuccess() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        // Then test login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testuser", "senha123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginEmpresaSuccess() throws Exception {
        // First create an empresa
        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createEmpresaJson("testempresa", "empresa@example.com", "12345678000100", "PEQUENO")))
                .andExpect(status().isOk());

        // Then test login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testempresa", "senha123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginInvalidUsername() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("nonexistentuser", "senha123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        // Then test login with wrong password
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testuser", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithEmptyUsername() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createInvalidLoginJson("username", "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithEmptyPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createInvalidLoginJson("senha", "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidJson() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createInvalidLoginJson("missing_field", "")))
                .andExpect(status().isBadRequest());
    }

    // Change Password Tests

    @Test
    @WithMockUser(username = "testuser")
    void testChangePasswordCandidatoSuccess() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        // Change password
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createChangePasswordJson("senha123", "newpassword123")))
                .andExpect(status().isOk());

        // Verify old password doesn't work
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testuser", "senha123")))
                .andExpect(status().isUnauthorized());

        // Verify new password works
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testuser", "newpassword123")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testempresa")
    void testChangePasswordEmpresaSuccess() throws Exception {
        // First create an empresa
        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createEmpresaJson("testempresa", "empresa@example.com", "12345678000100", "PEQUENO")))
                .andExpect(status().isOk());

        // Change password
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createChangePasswordJson("senha123", "newpassword123")))
                .andExpect(status().isOk());

        // Verify new password works
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testempresa", "newpassword123")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    void testChangePasswordUserNotFound() throws Exception {
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createChangePasswordJson("oldpass", "newpass")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testChangePasswordWithoutAuthentication() throws Exception {
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createChangePasswordJson("oldpass", "newpass")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testChangePasswordWithEmptyNewPassword() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createInvalidChangePasswordJson("novaSenha", "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testChangePasswordWithInvalidJson() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createInvalidChangePasswordJson("missing_field", "")))
                .andExpect(status().isBadRequest());
    }

    // Delete Account Tests

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteCandidatoAccountSuccess() throws Exception {
        // First create a candidato
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson("testuser", "test@example.com", "12345678901", "AUDITIVA")))
                .andExpect(status().isOk());

        // Delete account
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isOk());

        // Verify account is deleted - login should fail
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testuser", "senha123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testempresa")
    void testDeleteEmpresaAccountSuccess() throws Exception {
        // First create an empresa
        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createEmpresaJson("testempresa", "empresa@example.com", "12345678000100", "PEQUENO")))
                .andExpect(status().isOk());

        // Delete account
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isOk());

        // Verify account is deleted - login should fail
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson("testempresa", "senha123")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    void testDeleteAccountUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAccountWithoutAuthentication() throws Exception {
        mockMvc.perform(delete("/api/auth/delete-account"))
                .andExpect(status().isUnauthorized());
    }

    // Integration Tests

    @Test
    void testCompleteAuthWorkflowCandidato() throws Exception {
        String username = "integrationuser";
        String email = "integration@example.com";
        String cpf = "11111111111";
        String originalPassword = "senha123";
        String newPassword = "newpass456";

        // 1. Create candidato account
        mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createCandidatoJson(username, email, cpf, "VISUAL")))
                .andExpect(status().isOk());

        // 2. Login with original password
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson(username, originalPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        // Extract token for authenticated requests
        String response = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        String token = jsonNode.get("token").asText();

        // 3. Change password (authenticated)
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(TestDataFactory.createChangePasswordJson(originalPassword, newPassword)))
                .andExpect(status().isOk());

        // 4. Login with new password
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson(username, newPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // 5. Delete account (authenticated)
        mockMvc.perform(delete("/api/auth/delete-account")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // 6. Verify account is deleted
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson(username, newPassword)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCompleteAuthWorkflowEmpresa() throws Exception {
        String username = "integrationempresa";
        String email = "integration@empresa.com";
        String cnpj = "11111111000111";
        String originalPassword = "senha123";
        String newPassword = "newpass456";

        // 1. Create empresa account
        mockMvc.perform(post("/api/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createEmpresaJson(username, email, cnpj, "MEDIO")))
                .andExpect(status().isOk());

        // 2. Login and get token
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson(username, originalPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        String token = jsonNode.get("token").asText();

        // 3. Change password
        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(TestDataFactory.createChangePasswordJson(originalPassword, newPassword)))
                .andExpect(status().isOk());

        // 4. Delete account
        mockMvc.perform(delete("/api/auth/delete-account")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // 5. Verify account is deleted
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestDataFactory.createLoginJson(username, newPassword)))
                .andExpect(status().isUnauthorized());
    }
}
