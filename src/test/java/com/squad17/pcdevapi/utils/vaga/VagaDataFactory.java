package com.squad17.pcdevapi.utils.vaga;

public class VagaDataFactory {

    // Vaga JSON builders
    public static String createVagaJson(String nomeCargo, String descricao, String dataFimCandidatura) {
        return String.format("""
        {
          "nomeCargo": "%s",
          "descricao": "%s",
          "statusVaga": true,
          "dataFimCandidatura": "%s",
          "tags": ["Java", "Spring Boot"]
        }
        """, nomeCargo, descricao, dataFimCandidatura);
    }
}
