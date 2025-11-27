package com.example.minha_api_vendas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String email;
    private String telefone;

    @OneToMany(mappedBy = "vendedor")
    private List<Veiculo> veiculos;
}
