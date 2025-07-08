package com.squad17.pcdevapi.models.endereco;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @NotNull(message = "Rua é obrigatória")
    @Size(max = 250, message = "Rua deve ter no máximo 250 caracteres")
    @Column(name = "rua", length = 250, nullable = false)
    private String rua;

    @NotNull(message = "Bairro é obrigatório")
    @Size(max = 250, message = "Bairro deve ter no máximo 250 caracteres")
    @Column(name = "bairro", length = 250, nullable = false)
    private String bairro;

    @NotNull(message = "Cidade é obrigatória")
    @Size(max = 250, message = "Cidade deve ter no máximo 250 caracteres")
    @Column(name = "cidade", length = 250, nullable = false)
    private String cidade;

    @NotNull(message = "Estado é obrigatório")
    @Size(max = 2, message = "Estado deve ter no máximo 2 caracteres")
    @Column(name = "estado", length = 2, nullable = false)
    private String estado;

    @NotNull(message = "CEP é obrigatório")
    @Size(max = 8, message = "CEP deve ter no máximo 10 caracteres")
    @Column(name = "cep", length = 10, nullable = false)
    private String cep;

    @NotNull(message = "Número é obrigatório")
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @Size(max = 250, message = "Complemento deve ter no máximo 250 caracteres")
    @Column(name = "complemento", length = 250)
    private String complemento;

    @Size(max = 250, message = "Ponto de referência deve ter no máximo 250 caracteres")
    @Column(name = "ponto_referencia", length = 250)
    private String pontoReferencia;

    @NotNull(message = "País é obrigatório")
    @Size(max = 250, message = "País deve ter no máximo 250 caracteres")
    @Column(name = "pais", length = 250, nullable = false)
    private String pais;
}
