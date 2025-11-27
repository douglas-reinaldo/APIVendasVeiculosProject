package com.example.minha_api_vendas.repository;

import com.example.minha_api_vendas.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

}