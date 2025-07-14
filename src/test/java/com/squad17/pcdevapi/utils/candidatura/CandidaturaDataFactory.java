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
}
