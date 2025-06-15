package com.squad17.pcdevapi.models.habilidade;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habilidade")
@Data
@NoArgsConstructor
public class Habilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @NotNull(message = "Nome da habilidade é obrigatório")
    @Size(max = 250, message = "Nome da habilidade deve ter no máximo 250 caracteres")
    @Column(name = "nome", length = 250, nullable = false)
    private String nome;

    @NotNull(message = "Anos de experiência é obrigatório")
    @Column(name = "anos_experiencia", nullable = false)
    private Integer anosExperiencia;

    public Habilidade(String nome, Integer anosExperiencia) {
        this.nome = nome;
        this.anosExperiencia = anosExperiencia;
    }
}
