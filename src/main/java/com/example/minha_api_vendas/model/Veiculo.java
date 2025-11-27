package com.example.minha_api_vendas.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private int ano;
    private Double preco;
    private Boolean vendido;
}