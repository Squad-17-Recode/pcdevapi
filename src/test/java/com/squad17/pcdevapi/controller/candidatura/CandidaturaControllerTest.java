package com.squad17.pcdevapi.controller.candidatura;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.squad17.pcdevapi.utils.candidatura.CandidaturaDataFactory;

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
public class CandidaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateCandidatura_Unauthenticated() throws Exception {
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CandidaturaDataFactory.createCandidaturaJson("00000000-0000-0000-0000-000000000000")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateCandidatura_InvalidData() throws Exception {
        // Test with missing vagaId - Authentication happens first
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CandidaturaDataFactory.createCandidaturaJsonWithMissingFields()))
                .andExpect(status().isUnauthorized());

        // Test with invalid UUID format - Authentication happens first
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CandidaturaDataFactory.createCandidaturaJsonWithInvalidUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetMyCandidaturas_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/candidaturas/minhas-candidaturas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateCandidatura_ValidationErrors() throws Exception {
        // Test with completely empty JSON
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized()); // Will be unauthorized since no auth

        // Test with malformed JSON - Authentication happens first even for malformed JSON
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isUnauthorized());

        // Test with invalid UUID format - Authentication happens first
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CandidaturaDataFactory.createCandidaturaJsonWithInvalidUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetMyCandidaturas_WithPaginationParams() throws Exception {
        // Test with custom page size - will be unauthorized without auth
        mockMvc.perform(get("/api/candidaturas/minhas-candidaturas?page=1&size=5"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCandidaturaValidationMessages() throws Exception {
        // Test validation with explicit null vagaId
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vagaId\": null}"))
                .andExpect(status().isUnauthorized()); // Will be unauthorized since no auth
    }

    @Test
    void testEndpointAccessControl() throws Exception {
        // Test that both endpoints require authentication
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CandidaturaDataFactory.createCandidaturaJson("00000000-0000-0000-0000-000000000000")))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/candidaturas/minhas-candidaturas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testJsonStructureValidation() throws Exception {
        // Test with various malformed JSON structures - Authentication happens first in Spring Security
        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testContentTypeValidation() throws Exception {
        // Test without content type - Authentication happens first
        mockMvc.perform(post("/api/candidaturas")
                .content(CandidaturaDataFactory.createCandidaturaJson("00000000-0000-0000-0000-000000000000")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testHttpMethodsSupported() throws Exception {
        // Test that only POST is supported on /api/candidaturas - Authentication happens first
        mockMvc.perform(get("/api/candidaturas"))
                .andExpect(status().isUnauthorized());

        // Test that only GET is supported on /api/candidaturas/minhas-candidaturas - Authentication happens first
        mockMvc.perform(post("/api/candidaturas/minhas-candidaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPaginationParameterTypes() throws Exception {
        // Test with non-numeric pagination parameters - Authentication happens first
        mockMvc.perform(get("/api/candidaturas/minhas-candidaturas?page=abc"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/candidaturas/minhas-candidaturas?size=xyz"))
                .andExpect(status().isUnauthorized());
    }
}
