package com.squad17.pcdevapi.models.contato;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contato")
@NoArgsConstructor
@Data
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Número de telefone é obrigatório")
    @Column(name = "numero_telefone", length = 20, nullable = false)
    private String numeroTelefone;

    public Contato(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}
