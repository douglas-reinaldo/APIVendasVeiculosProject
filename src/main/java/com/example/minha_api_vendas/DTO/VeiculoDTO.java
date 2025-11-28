package com.example.minha_api_vendas.DTO;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class VeiculoDTO {

    private String marca;
    private String modelo;
    private int ano;
    private Double preco;
    private Boolean vendido;

    @NotNull
    private Long vendedorId;
}