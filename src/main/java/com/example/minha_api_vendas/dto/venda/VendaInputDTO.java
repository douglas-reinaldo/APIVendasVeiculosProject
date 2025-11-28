package com.example.minha_api_vendas.dto.venda;

import lombok.Data;

@Data
public class VendaInputDTO {
    private Long veiculoId;
    private Long vendedorId;
    private Double valorTotal;
}
