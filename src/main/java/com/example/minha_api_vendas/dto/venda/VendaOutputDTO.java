package com.example.minha_api_vendas.dto.venda;

import lombok.Data;

import java.time.*;

@Data
public class VendaOutputDTO {
    private Long id;
    private String vendedorNome;
    private String veiculoModelo;
    private LocalDate dataVenda;
    private Double valorFinal;
}
