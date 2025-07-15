package com.squad17.pcdevapi.models.habilidade;

import java.util.UUID;

import com.squad17.pcdevapi.models.candidato.Candidato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Nome da habilidade é obrigatório")
    @Size(max = 250, message = "Nome da habilidade deve ter no máximo 250 caracteres")
    @Column(name = "nome", length = 250, nullable = false)
    private String nome;

    @NotNull(message = "Anos de experiência é obrigatório")
    @Min(value = 0, message = "Anos de experiência deve ser maior ou igual a 0")
    @Column(name = "anos_experiencia", nullable = false)
    private Integer anosExperiencia;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;

    public Habilidade(String nome, Integer anosExperiencia) {
        this.nome = nome;
        this.anosExperiencia = anosExperiencia;
    }
}
