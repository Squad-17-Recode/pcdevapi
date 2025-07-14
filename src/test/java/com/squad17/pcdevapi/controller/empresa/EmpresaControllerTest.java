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

import com.squad17.pcdevapi.utils.empresa.EmpresaDataFactory;
import com.squad17.pcdevapi.utils.vaga.VagaDataFactory;

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
        String json = EmpresaDataFactory.createEmpresaJson("empresaTest", "empresaTest@example.com", "12345678000199", "GRANDE");

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEmpresaById() throws Exception {
        String json = EmpresaDataFactory.createEmpresaJson("empresaTest2", "empresaTest2@example.com", "12345678000198", "MEDIO");

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
    void testGetEmpresaByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/empresas/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmpresaDuplicateUsername() throws Exception {
        String json = EmpresaDataFactory.createEmpresaJson("dupempresa", "dupempresa@example.com", "11111111000111", "PEQUENO");

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same username
        String jsonDup = EmpresaDataFactory.createEmpresaJson("dupempresa", "other@example.com", "22222222000222", "MEDIO");

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEmpresaDuplicateEmail() throws Exception {
        String json = EmpresaDataFactory.createEmpresaJson("emailempresa", "emailempresa@example.com", "33333333000333", "PEQUENO");

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());

        // Try to create again with same email
        String jsonDup = EmpresaDataFactory.createEmpresaJson("otherempresa", "emailempresa@example.com", "44444444000444", "MEDIO");

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(jsonDup))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEmpresaWithInvalidData() throws Exception {
        // Test with missing username
        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(EmpresaDataFactory.createInvalidEmpresaJson("username", "")))
                .andExpect(status().isBadRequest());

        // Test with invalid email format
        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(EmpresaDataFactory.createInvalidEmpresaJson("email", "invalid-email-format")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEmpresas() throws Exception {
        // Create an empresa first
        String json = EmpresaDataFactory.createEmpresaJson("getallempresa", "getallempresa@example.com", "77777777000777", "PEQUENO");

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
        String json1 = EmpresaDataFactory.createEmpresaJson("empresa1", "empresa1@example.com", "88888888000888", "PEQUENO");
        String json2 = EmpresaDataFactory.createEmpresaJson("empresa2", "empresa2@example.com", "99999999000999", "MEDIO");

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
        // Test all RangeFuncionarios values
        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(EmpresaDataFactory.createEmpresaJson("empresapequena", "pequena@example.com", "10101010000101", "PEQUENO")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(EmpresaDataFactory.createEmpresaJson("empresamedia", "media@example.com", "20202020000202", "MEDIO")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/empresas")
                .contentType("application/json")
                .content(EmpresaDataFactory.createEmpresaJson("empresagrande", "grande@example.com", "30303030000303", "GRANDE")))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateVaga_Unauthenticated() throws Exception {
        String json = VagaDataFactory.createVagaJson("Desenvolvedor Backend", "Vaga para desenvolvedor Java.", "2025-08-01");

        mockMvc.perform(post("/api/empresa/vaga")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }
}
