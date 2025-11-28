package com.example.minha_api_vendas.dto.veiculo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;


@Data
public class VeiculoInputDTO {

    private String marca;
    private String modelo;
    private int ano;
    private Double preco;
    private Boolean vendido;

    @NotNull
    private Long vendedorId;
}