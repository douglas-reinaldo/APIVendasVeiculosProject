package com.example.minha_api_vendas.dto.veiculo;

import lombok.Data;

@Data
public class VeiculoDTO {

    private Long id;
    private String marca;
    private String modelo;
    private int ano;
    private Double preco;
    private String placa;
    private Boolean vendido;
}