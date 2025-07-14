package com.squad17.pcdevapi.utils.auth;

public class AuthDataFactory {

    // Auth JSON builders
    public static String createLoginJson(String username, String senha) {
        return String.format("""
        {
          "username": "%s",
          "senha": "%s"
        }
        """, username, senha);
    }

    public static String createChangePasswordJson(String senhaAtual, String novaSenha) {
        return String.format("""
        {
          "senhaAtual": "%s",
          "novaSenha": "%s"
        }
        """, senhaAtual, novaSenha);
    }

    // Auth invalid JSON builders
    public static String createInvalidLoginJson(String field, String value) {
        return switch (field) {
            case "username" -> String.format("""
            {
              %s
              "senha": "senha123"
            }
            """, value.isEmpty() ? "" : "\"username\": \"" + value + "\",");
            case "senha" -> String.format("""
            {
              "username": "testuser",
              %s
            }
            """, value.isEmpty() ? "" : "\"senha\": \"" + value + "\"");
            case "missing_field" -> """
            {
              "username": "testuser"
            }
            """;
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }

    public static String createInvalidChangePasswordJson(String field, String value) {
        return switch (field) {
            case "senhaAtual" -> String.format("""
            {
              %s
              "novaSenha": "newpass123"
            }
            """, value.isEmpty() ? "" : "\"senhaAtual\": \"" + value + "\",");
            case "novaSenha" -> String.format("""
            {
              "senhaAtual": "oldpass123",
              %s
            }
            """, value.isEmpty() ? "" : "\"novaSenha\": \"" + value + "\"");
            case "missing_field" -> """
            {
              "senhaAtual": "oldpass123"
            }
            """;
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }
}
