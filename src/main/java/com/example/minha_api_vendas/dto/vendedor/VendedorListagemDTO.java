package com.example.minha_api_vendas.dto.vendedor;

import lombok.Data;

@Data
public class VendedorListagemDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
}