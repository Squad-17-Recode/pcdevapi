package com.squad17.pcdevapi.models.contato;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String numeroTelefone;

    public Contato() {
    }

    public Contato(UUID id, String numeroTelefone) {
        this.id = id;
        this.numeroTelefone = numeroTelefone;
    }
}
