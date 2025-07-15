package com.squad17.pcdevapi.models.contato;

import java.util.UUID;

import com.squad17.pcdevapi.models.conta.Conta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Número de telefone é obrigatório")
    @Column(name = "numero_telefone", length = 20, nullable = false)
    private String numeroTelefone;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    public Contato(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}
