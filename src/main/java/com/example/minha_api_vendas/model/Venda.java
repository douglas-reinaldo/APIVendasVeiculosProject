package com.example.minha_api_vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "O veiculo não pode ser nulo")
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @NotNull(message = "O vendedor não pode ser nulo")
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @NotNull(message = "A data da venda é obrigatoria")
    @Column(nullable = false)
    private LocalDate dataVenda;

    @Positive(message = "O valor final deve ser positivo")
    @NotNull(message = "O valor final é obrigatorio")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal;
}
