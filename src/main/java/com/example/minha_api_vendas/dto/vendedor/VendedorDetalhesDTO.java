package com.example.minha_api_vendas.dto.vendedor;

import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import lombok.Data;
import java.util.List;

@Data
public class VendedorDetalhesDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private List<VeiculoOutputDTO> veiculos;
}