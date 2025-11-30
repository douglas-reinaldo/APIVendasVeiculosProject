package com.example.minha_api_vendas.dto.venda;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class VendaInputDTO {

    @NotNull(message = "Veiculo é obrigatorio")
    private Long veiculoId;

    @NotNull(message = "Vendedor é obrigatorio")
    private Long vendedorId;

    @Positive(message = "Valor total deve ser maior que 0")
    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;
}
