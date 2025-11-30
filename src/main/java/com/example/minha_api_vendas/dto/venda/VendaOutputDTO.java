package com.example.minha_api_vendas.dto.venda;

import com.example.minha_api_vendas.model.Venda;
import lombok.Data;

import java.math.BigDecimal;
import java.time.*;

@Data
public class VendaOutputDTO {
    private Long id;
    private String veiculoModelo;
    private String vendedorNome;
    private LocalDate dataVenda;
    private BigDecimal valorFinal;

    public VendaOutputDTO(Venda venda) {
        this.id = venda.getId();
        this.veiculoModelo = venda.getVeiculo().getModelo();
        this.vendedorNome = venda.getVendedor().getNome();
        this.dataVenda = venda.getDataVenda();
        this.valorFinal = venda.getValorFinal();
    }

    public  VendaOutputDTO() {
    }
}

