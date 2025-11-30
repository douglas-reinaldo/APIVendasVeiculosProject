package com.example.minha_api_vendas.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatorio")
    @Column(nullable = false, length = 50)
    private String nome;

    @NotBlank(message = "O email é obrigatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank(message = "O telefone é obrigatorio")
    @Column(nullable = false, unique = true, length = 15)
    private String telefone;

    @OneToMany(mappedBy = "vendedor")
    @JsonManagedReference
    private List<Veiculo> veiculos = new ArrayList<>();
}
