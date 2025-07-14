package com.squad17.pcdevapi.utils.candidatura;

public class CandidaturaDataFactory {

    // Candidatura JSON builders
    public static String createCandidaturaJson(String vagaId) {
        return String.format("""
        {
          "vagaId": "%s"
        }
        """, vagaId);
    }

    public static String createInvalidCandidaturaJson(String field, String value) {
        if ("vagaId".equals(field)) {
            return String.format("""
            {
              "vagaId": %s
            }
            """, value.equals("") ? "null" : "\"" + value + "\"");
        }
        return """
        {
          "vagaId": "00000000-0000-0000-0000-000000000000"
        }
        """;
    }

    public static String createCandidaturaJsonWithMissingFields() {
        return """
        {
        }
        """;
    }

    public static String createCandidaturaJsonWithInvalidUUID() {
        return """
        {
          "vagaId": "invalid-uuid-format"
        }
        """;
    }
}
