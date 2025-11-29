package com.example.minha_api_vendas.dto.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class VeiculoInputDTO {

    @NotBlank(message = "Marca é obrigatoria")
    @Size(min = 2, max = 50)
    private String marca;

    @NotBlank(message = "Modelo é obrigatorio")
    @Size(min = 2, max = 50)
    private String modelo;

    @NotNull(message = "")
    @Min(value = 1900, message = "Ano deve ser maior que 1900")
    private Integer ano;

    @NotNull(message = "Preço é obrigatorio")
    @Positive(message = "O preço deve ser maior que zero")
    private Double preco;

    @NotBlank(message = "Placa é obrigatoria")
    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$",
            message = "Placa deve estar no formato ABC-1234 ou ABC1D23 (Mercosul)")
    private String placa;


    @NotNull(message = "Vendedor é obrigatorio")
    private Long vendedorId;
}

