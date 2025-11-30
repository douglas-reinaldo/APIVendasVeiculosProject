package com.example.minha_api_vendas.dto.veiculo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VeiculoDTO {

    private Long id;
    private String marca;
    private String modelo;
    private int ano;
    private BigDecimal preco;
    private String placa;
    private Boolean vendido;
}