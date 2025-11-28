package com.example.minha_api_vendas.repository;

import com.example.minha_api_vendas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda,Long> {

}
