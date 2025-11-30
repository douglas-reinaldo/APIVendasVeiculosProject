package com.example.minha_api_vendas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Data
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A marca é obrigatoria")
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank(message = "O modelo é obrigatorio")
    @Column(nullable = false, length = 50)
    private String modelo;

    @NotNull(message = "O ano é obrigatorio")
    @Min(value = 1900, message = "Ano inválido")
    @Max(value = 2030, message = "Ano inválido")
    @Column(nullable = false)
    private int ano;

    @NotNull(message = "O preço é obrigatorio")
    @Positive(message = "O preço deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull(message = "O status da venda deve ser definido")
    @Column(nullable = false)
    private Boolean vendido = false;

    @NotBlank(message = "A placa é obrigatoria")
    @Size(min = 7, max = 8)
    @Column(nullable = false, unique = true,  length = 8)
    private String placa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    @JsonBackReference
    private Vendedor vendedor;
}