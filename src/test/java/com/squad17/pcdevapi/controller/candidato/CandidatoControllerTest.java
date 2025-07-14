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

import com.squad17.pcdevapi.utils.candidato.CandidatoDataFactory;

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
        String json = CandidatoDataFactory.createCandidatoJson("testuser", "testuser@example.com", "12345678901", "AUDITIVA");

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCandidatoById() throws Exception {
        String json = CandidatoDataFactory.createCandidatoJson("testuser2", "testuser2@example.com", "12345678902", "AUDITIVA");

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
        String json = CandidatoDataFactory.createCandidatoJson("dupuser", "dupuser@example.com", "12345678905", "AUDITIVA");

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same username
        String jsonDup = CandidatoDataFactory.createCandidatoJson("dupuser", "other@example.com", "12345678906", "AUDITIVA");

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoDuplicateEmail() throws Exception {
        String json = CandidatoDataFactory.createCandidatoJson("useremail", "useremail@example.com", "12345678907", "AUDITIVA");

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same email
        String jsonDup = CandidatoDataFactory.createCandidatoJson("otheruser", "useremail@example.com", "12345678908", "AUDITIVA");

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithInvalidData() throws Exception {
        // Test with missing username
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createInvalidCandidatoJson("username", "")))
                .andExpect(status().isBadRequest());

        // Test with invalid email format
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createInvalidCandidatoJson("email", "invalid-email-format")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllCandidatos() throws Exception {
        // Create a candidato first
        String json = CandidatoDataFactory.createCandidatoJson("getalltest", "getalltest@example.com", "12345678909", "AUDITIVA");

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
        String json1 = CandidatoDataFactory.createCandidatoJson("user1", "user1@example.com", "12345678903", "AUDITIVA");
        String json2 = CandidatoDataFactory.createCandidatoJson("user2", "user2@example.com", "12345678904", "AUDITIVA");

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
        // Test all TipoDeficiencia values
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJson("candidatoauditiva", "auditiva@example.com", "12345678911", "AUDITIVA")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJson("candidatovisual", "visual@example.com", "12345678912", "VISUAL")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJson("candidatofisica", "fisica@example.com", "12345678913", "FISICA")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJson("candidatointelectual", "intelectual@example.com", "12345678914", "INTELECTUAL")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJson("candidatomultipla", "multipla@example.com", "12345678915", "MULTIPLA")))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithEmptyCollections() throws Exception {
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJsonWithEmptyCollections()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithMultipleContacts() throws Exception {
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJsonWithMultipleData()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithNullOptionalFields() throws Exception {
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJsonMinimal()))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatoWithInvalidTipoDeficiencia() throws Exception {
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJsonWithInvalidTipoDeficiencia()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCandidatoWithInvalidHabilidadeData() throws Exception {
        mockMvc.perform(post("/api/candidatos")
                .contentType("application/json")
                .content(CandidatoDataFactory.createCandidatoJsonWithInvalidHabilidade()))
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
            mockMvc.perform(post("/api/candidatos")
                    .contentType("application/json")
                    .content(CandidatoDataFactory.createCandidatoJsonForIteration(i)))
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
